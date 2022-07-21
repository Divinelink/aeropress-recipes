package gr.divinelink.core.util.extensions

import android.graphics.drawable.Drawable
import android.view.MenuItem

fun MenuItem.setDisabled() {
    this.isEnabled = false
    this.icon.setDisabled()
}

fun MenuItem.setEnabled() {
    this.isEnabled = true
    this.icon.setEnabled()
}

fun Drawable.setDisabled() {
    this.alpha = 100
}

fun Drawable.setEnabled() {
    this.alpha = 255
}
