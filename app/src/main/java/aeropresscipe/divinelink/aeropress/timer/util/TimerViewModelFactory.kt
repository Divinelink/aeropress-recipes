package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.timer.ITimerViewModel
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class TimerViewModelFactory(
    private val app: Application,
    private var delegate: WeakReference<ITimerViewModel>?,
    private val repository: TimerRepository)
    : ViewModelProvider.AndroidViewModelFactory(app) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(app, delegate, repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}