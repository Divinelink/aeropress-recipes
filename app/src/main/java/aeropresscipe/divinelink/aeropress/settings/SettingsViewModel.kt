package aeropresscipe.divinelink.aeropress.settings

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class SettingsViewModel @AssistedInject constructor(
    private var repository: TimerRepository,
    @Assisted override var delegate: WeakReference<ISettingsViewModel>? = null
) : BaseViewModel<ISettingsViewModel>(),
    SettingsIntents {
    internal var statesList: MutableList<SettingsState> = mutableListOf()

    var state: SettingsState = SettingsState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
            statesList.add(value)
        }

}

interface ISettingsViewModel {
    fun updateState(state: SettingsState)
}

interface SettingsIntents : MVIBaseView {
    //TODO add your code here
}

sealed class SettingsState {
    object InitialState : SettingsState()
    object LoadingState : SettingsState()
    data class ErrorState(val data: String) : SettingsState()
}

interface SettingsStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val assistedFactory: SettingsViewModelAssistedFactory,
    private val delegate: WeakReference<ISettingsViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface SettingsViewModelAssistedFactory {
    fun create(delegate: WeakReference<ISettingsViewModel>?): SettingsViewModel
}
