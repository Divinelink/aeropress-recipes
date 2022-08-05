package aeropresscipe.divinelink.aeropress.finish

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.LikeSnackBar
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference


class FinishViewModel @AssistedInject constructor(
    private var repository: TimerRepository,
    @Assisted override var delegate: WeakReference<IFinishViewModel>? = null
) : BaseViewModel<IFinishViewModel>(),
    FinishIntents {

    internal var statesList: MutableList<FinishState> = mutableListOf()

    var state: FinishState = FinishState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
            statesList.add(value)
        }

    init {
        state = FinishState.InitialState
    }

    override fun closeButtonClicked() {
        state = FinishState.CloseState
    }

    override fun likeRecipe(recipe: Recipe?) {
        if (recipe != null) {
            repository.likeCurrentRecipe(recipe) { recipeLiked ->
                if (recipeLiked) {
                    state = FinishState.RecipeSavedState
                    state = FinishState.ShowSnackBar(LikeSnackBar.Like)
                } else {
                    state = FinishState.RecipeRemovedState
                    state = FinishState.ShowSnackBar(LikeSnackBar.Remove)
                }
            }
        }

    }

}

interface IFinishViewModel {
    fun updateState(state: FinishState)
}

interface FinishIntents : MVIBaseView {
    fun closeButtonClicked()
    fun likeRecipe(recipe: Recipe?)
}

sealed class FinishState {
    object InitialState : FinishState()
    object LoadingState : FinishState()
    object CloseState : FinishState()
    data class ErrorState(val data: String) : FinishState()

    object RecipeSavedState : FinishState()
    object RecipeRemovedState : FinishState()

    data class UpdateSavedIndicator(val frame: Int) : FinishState()
    data class ShowSnackBar(val value: LikeSnackBar) : FinishState()
}

interface FinishStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState(state: FinishState.ErrorState)
    fun handleCloseState()

    fun handleRecipeSavedState()
    fun handleRecipeRemovedState()

    fun handleUpdateSavedIndicator(state: FinishState.UpdateSavedIndicator)
    fun handleShowSnackBar(state: FinishState.ShowSnackBar)
}

