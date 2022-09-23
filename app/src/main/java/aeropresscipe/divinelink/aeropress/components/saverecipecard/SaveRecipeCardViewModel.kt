package aeropresscipe.divinelink.aeropress.components.saverecipecard

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.LIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.history.LikeSnackBar
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class SaveRecipeCardViewModel @AssistedInject constructor(
    private var repository: TimerRepository,
    @Assisted public override var delegate: WeakReference<ISaveRecipeCardViewModel>? = null
) : BaseViewModel<ISaveRecipeCardViewModel>(),
    SaveRecipeCardIntents {
    internal var statesList: MutableList<SaveRecipeCardState> = mutableListOf()

    var state: SaveRecipeCardState = SaveRecipeCardState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
            statesList.add(value)
        }

    override fun init(recipe: Recipe?) {
        state = SaveRecipeCardState.InitialState
        repository.isRecipeSaved(recipe) { saved ->
            val frame = when (saved) {
                true -> LIKE_MAX_FRAME
                false -> DISLIKE_MAX_FRAME
            }
            state = SaveRecipeCardState.UpdateSavedIndicator(frame)
        }
    }

    override fun likeRecipe(recipe: Recipe?) {
        if (recipe != null) {
            repository.likeCurrentRecipe(recipe) { recipeLiked ->
                if (recipeLiked) {
                    state = SaveRecipeCardState.RecipeSavedState
                    state = SaveRecipeCardState.ShowSnackBar(LikeSnackBar.Like)
                } else {
                    state = SaveRecipeCardState.RecipeRemovedState
                    state = SaveRecipeCardState.ShowSnackBar(LikeSnackBar.Remove)
                }
            }
        }
    }
}

interface ISaveRecipeCardViewModel {
    fun updateState(state: SaveRecipeCardState)
}

interface SaveRecipeCardIntents : MVIBaseView {
    fun init(recipe: Recipe?)
    fun likeRecipe(recipe: Recipe?)
}

sealed class SaveRecipeCardState {
    object InitialState : SaveRecipeCardState()

    object RecipeSavedState : SaveRecipeCardState()
    object RecipeRemovedState : SaveRecipeCardState()

    data class UpdateSavedIndicator(val frame: Int) : SaveRecipeCardState()
    data class ShowSnackBar(val value: LikeSnackBar) : SaveRecipeCardState()
}

interface SaveRecipeCardStateHandler {
    fun handleInitialState()

    fun handleRecipeSavedState()
    fun handleRecipeRemovedState()

    fun handleUpdateSavedIndicator(state: SaveRecipeCardState.UpdateSavedIndicator)
    fun handleShowSnackBar(state: SaveRecipeCardState.ShowSnackBar)
}

@Suppress("UNCHECKED_CAST")
class SaveRecipeCardViewModelFactory(
    private val assistedFactory: SaveRecipeCardViewModelAssistedFactory,
    private val delegate: WeakReference<ISaveRecipeCardViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SaveRecipeCardViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface SaveRecipeCardViewModelAssistedFactory {
    fun create(delegate: WeakReference<ISaveRecipeCardViewModel>?): SaveRecipeCardViewModel
}
