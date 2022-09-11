package aeropresscipe.divinelink.aeropress.finish

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.AssistedFactory
import java.lang.ref.WeakReference

@Suppress("UNCHECKED_CAST")
class FinishViewModelFactory(
    private val assistedFactory: FinishViewModelAssistedFactory,
    private val delegate: WeakReference<IFinishViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface FinishViewModelAssistedFactory {
    fun create(delegate: WeakReference<IFinishViewModel>?): FinishViewModel
}
