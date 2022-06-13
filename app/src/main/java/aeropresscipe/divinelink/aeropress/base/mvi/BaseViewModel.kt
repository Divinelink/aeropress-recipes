package aeropresscipe.divinelink.aeropress.base.mvi

import java.lang.ref.WeakReference

abstract class BaseViewModel<T : Any> : MVIBaseView {
    protected abstract var delegate: WeakReference<T>?

    override fun onDestroy() {
        delegate = null
    }
}

