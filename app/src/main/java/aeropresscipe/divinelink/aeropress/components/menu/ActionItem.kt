package aeropresscipe.divinelink.aeropress.components.menu

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class ActionItem @JvmOverloads constructor(
    @DrawableRes val iconRes: Int,
    val title: CharSequence,
    @ColorRes val tintRes: Int = R.color.colorOnSurface,
    val action: Runnable,
)
