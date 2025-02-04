package aeropresscipe.divinelink.aeropress.components.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.divinelink.aeropress.recipes.databinding.ViewNotificationBinding
import com.google.android.material.snackbar.ContentViewCallback

class NotificationView :
  FrameLayout,
  ContentViewCallback {
  private var binding = ViewNotificationBinding.inflate(LayoutInflater.from(context), this, false)

  override fun animateContentIn(
    delay: Int,
    duration: Int,
  ) {
    // Intentionally Empty
  }

  override fun animateContentOut(
    delay: Int,
    duration: Int,
  ) {
    // Intentionally Empty
  }

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr,
  ) {

    addView(binding.root)
  }

  fun setText(@StringRes resId: Int) {
    setText(context.getText(resId))
  }

  fun setText(text: CharSequence) {
    binding.notificationText.text = text
  }
}
