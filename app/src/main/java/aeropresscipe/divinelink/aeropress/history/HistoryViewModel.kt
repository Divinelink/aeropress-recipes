package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date

class HistoryViewModel @AssistedInject constructor(
    private var repository: HistoryRepository,
    @Assisted override var delegate: WeakReference<IHistoryViewModel>? = null
) : BaseViewModel<IHistoryViewModel>(),
    HistoryIntents {
    internal var statesList: MutableList<HistoryState> = mutableListOf()

    var state: HistoryState = HistoryState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
        }

    init {
        state = HistoryState.InitialState
        repository.getHistory {
            state = if (it.isEmpty()) {
                HistoryState.EmptyHistoryState
            } else {
                HistoryState.ShowHistoryState(it)
            }
        }
    }

    override fun startBrew(recipe: Recipe) {
        repository.startBrew(recipe) {
            state = if (it == null) {
                HistoryState.ErrorState("Something went wrong!") // //TODO 15/6/22 divinelink: Fix Error State
            } else {
                recipe.isNewRecipe = true
                HistoryState.StartNewBrewState(recipe)
            }
        }
    }

    override fun likeRecipe(recipe: History, position: Int) {
        repository.likeRecipe(SavedRecipeDomain(recipe.recipe, getCurrentDate())) {
            state = HistoryState.RecipeLikedState(it, position)
            val value: LikeSnackBar = when {
                it.isRecipeLiked -> LikeSnackBar.Like
                else -> LikeSnackBar.Remove
            }
            state = HistoryState.ShowSnackBar(value)
        }
    }

    override fun clearHistory(delete: Boolean) {
        when (delete) {
            true -> repository.clearHistory {
                state = HistoryState.EmptyHistoryState
            }
            false -> state = HistoryState.ClearHistoryPopUpState
        }
    }

    fun getCurrentDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("d MMMM yyyy")
        return formatter.format(date.time)
    }
}

interface IHistoryViewModel {
    fun updateState(state: HistoryState)
}

interface HistoryIntents : MVIBaseView {
    fun startBrew(recipe: Recipe)
    fun likeRecipe(recipe: History, position: Int)
    fun clearHistory(delete: Boolean)
}

sealed class HistoryState {
    object InitialState : HistoryState()
    object LoadingState : HistoryState()
    data class ErrorState(val data: String) : HistoryState()

    data class ShowHistoryState(val list: List<History>) : HistoryState()
    object EmptyHistoryState : HistoryState()

    data class StartNewBrewState(val recipe: Recipe) : HistoryState()

    data class RecipeLikedState(val item: History, val position: Int) : HistoryState()
    object ClearHistoryPopUpState : HistoryState()

    data class ShowSnackBar(val value: LikeSnackBar) : HistoryState()
}

interface HistoryStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState(state: HistoryState.ErrorState)

    fun handleShowHistoryState(state: HistoryState.ShowHistoryState)
    fun handleEmptyHistoryState()

    fun handleStartNewBrewState(state: HistoryState.StartNewBrewState)

    fun handleRecipeLikedState(state: HistoryState.RecipeLikedState)
    fun handleClearHistoryPopUpState()

    fun handleShowSnackBar(state: HistoryState.ShowSnackBar)
}

@Suppress("UNCHECKED_CAST")
class HistoryViewModelFactory(
    private val assistedFactory: HistoryViewModelAssistedFactory,
    private val delegate: WeakReference<IHistoryViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface HistoryViewModelAssistedFactory {
    fun create(delegate: WeakReference<IHistoryViewModel>?): HistoryViewModel
}
