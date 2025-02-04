package aeropresscipe.divinelink.aeropress.base.mvi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import java.lang.ref.WeakReference

abstract class BaseAndroidViewModel<T : Any>(application: Application) :
  AndroidViewModel(application),
  MVIBaseView {
  protected abstract var delegate: WeakReference<T>?

  override fun onDestroy() {
    delegate = null
  }
}
