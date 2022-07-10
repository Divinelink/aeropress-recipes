package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.timer.ITimerViewModel
import aeropresscipe.divinelink.aeropress.timer.TimerViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class TimerViewModelFactory(
    private val assistedFactory: TimerViewModelAssistedFactory,
    private val delegate: WeakReference<ITimerViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface TimerViewModelAssistedFactory {
    fun create(delegate: WeakReference<ITimerViewModel>?): TimerViewModel
}
