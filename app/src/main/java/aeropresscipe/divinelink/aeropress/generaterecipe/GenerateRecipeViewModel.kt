package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class GenerateRecipeViewModel @AssistedInject constructor(
//    private var repository: GenerateRecipeRepository,
    @Assisted override var delegate: WeakReference<IGenerateRecipeViewModel>? = null
) : BaseViewModel<IGenerateRecipeViewModel>(),
    GenerateRecipeIntents {
    internal var statesList: MutableList<GenerateRecipeState> = mutableListOf()

    var state: GenerateRecipeState = GenerateRecipeState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
        }

}

interface IGenerateRecipeViewModel {
    fun updateState(state: GenerateRecipeState)
}

interface GenerateRecipeIntents : MVIBaseView {
    //TODO add your code here
}

sealed class GenerateRecipeState {
    object InitialState : GenerateRecipeState()
    object LoadingState : GenerateRecipeState()
    data class ErrorState(val data: String) : GenerateRecipeState()
}

interface GenerateRecipeStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()
}

@Suppress("UNCHECKED_CAST")
class GenerateRecipeViewModelFactory(
    private val assistedFactory: GenerateRecipeViewModelAssistedFactory,
    private val delegate: WeakReference<IGenerateRecipeViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenerateRecipeViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface GenerateRecipeViewModelAssistedFactory {
    fun create(delegate: WeakReference<IGenerateRecipeViewModel>?): GenerateRecipeViewModel
}
