package gr.divinelink.core.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View


fun View?.fadeOut() {
    this?.animate()
        ?.alpha(0f)
        ?.setDuration(500L)
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@fadeOut.visibility = View.GONE
            }
        })
}

