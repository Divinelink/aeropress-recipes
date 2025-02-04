package aeropresscipe.divinelink.aeropress.base.mvi

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<T : Any> :
  ViewModel(),
  MVIBaseView {
  protected abstract var delegate: WeakReference<T>?

  override fun onDestroy() {
    delegate = null
  }
}
