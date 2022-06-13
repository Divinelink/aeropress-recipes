package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import java.lang.ref.WeakReference

class SavedRecipesViewModel(
    override var delegate: WeakReference<ISavedRecipesViewModel>?,
) : BaseViewModel<ISavedRecipesViewModel>(), SavedRecipesIntents {
    internal var statesList: MutableList<SavedRecipesState> = mutableListOf()

    var state: SavedRecipesState = SavedRecipesState.InitialState
        set(value) {
            field = value
            delegate?.get()?.updateSavedRecipesState(value)
        }
}

interface ISavedRecipesViewModel {
    fun updateSavedRecipesState(state: SavedRecipesState)
}

interface SavedRecipesIntents : MVIBaseView {
    //TODO add your code here
}

sealed class SavedRecipesState {
    object InitialState : SavedRecipesState()
    object LoadingState : SavedRecipesState()
    data class ErrorState(val data: String) : SavedRecipesState()
}

interface SavedRecipesStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()
}
