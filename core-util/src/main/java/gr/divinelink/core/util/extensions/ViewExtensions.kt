package gr.divinelink.core.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.TextView


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

infix fun TextView?.updateTextWithFade(text: CharSequence) {
    this?.animate()
        ?.alpha(0f)
        ?.setDuration(500L)
        ?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@updateTextWithFade.text = text
                this@updateTextWithFade.animate()
                    ?.alpha(1f)?.duration = 500L
            }
        })
}

fun View.padding(left: Int = paddingLeft, top: Int = paddingTop, right: Int = paddingRight, bottom: Int = paddingBottom) {
    setPadding(left, top, right, bottom)
}
