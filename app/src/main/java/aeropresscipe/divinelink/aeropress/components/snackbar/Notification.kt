package aeropresscipe.divinelink.aeropress.components.snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.divinelink.aeropress.recipes.R
import com.google.android.material.snackbar.BaseTransientBottomBar

class Notification(
  parent: ViewGroup,
  content: NotificationView,
) : BaseTransientBottomBar<Notification>(parent, content, content) {

  init {
    getView().setBackgroundColor(ContextCompat.getColor(view.context, R.color.transparent))
    getView().setPadding(0, 0, 0, 0)
  }

  companion object {
    fun make(
      view: View?,
      text: CharSequence,
    ): Notification {
      // First we find a suitable parent for our custom view
      val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
        "No suitable parent found from the given view. Please provide a valid view.",
      )

      // Inflate the custom view
      val customView = LayoutInflater.from(view?.context).inflate(
        R.layout.view_custom_notification,
        parent,
        false,
      ) as NotificationView

      customView.setText(text)

      // We create and return our Snackbar
      return Notification(parent, customView)
    }

    fun make(
      view: View?,
      @StringRes resId: Int,
    ): Notification = make(view, view?.resources?.getText(resId).toString())
  }
}

internal fun View?.findSuitableParent(): ViewGroup? {
  var view = this
  var fallback: ViewGroup? = null
  do {
    if (view is CoordinatorLayout) {
      // We've found a CoordinatorLayout, use it
      return view
    } else if (view is FrameLayout) {
      if (view.id == android.R.id.content) {
        // If we've hit the decor content view, then we didn't find a CoL in the
        // hierarchy, so use it.
        return view
      } else {
        // It's not the content view but we'll use it as our fallback
        fallback = view
      }
    }

    if (view != null) {
      // Else, we will loop and crawl up the view hierarchy and try to find a parent
      val parent = view.parent
      view = if (parent is View) parent else null
    }
  } while (view != null)

  // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
  return fallback
}
