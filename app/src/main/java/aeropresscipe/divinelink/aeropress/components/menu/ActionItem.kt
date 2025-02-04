package aeropresscipe.divinelink.aeropress.components.menu

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.divinelink.aeropress.recipes.R

data class ActionItem @JvmOverloads constructor(
  @DrawableRes val iconRes: Int,
  val title: CharSequence,
  @ColorRes val tintRes: Int = R.color.colorOnSurface,
  val action: Runnable,
)
