package aeropresscipe.divinelink.aeropress.ui.components.swipeable

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.aeropress.recipes.R

data class SwipeUiModel(
  val swipeAction: SwipeAction,
  val icon: ImageVector,
  @StringRes val descriptionRes: Int,
  val getColor: @Composable () -> Color,
  val staysDismissed: Boolean,
) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is SwipeUiModel) return false

    if (swipeAction != other.swipeAction) return false
    if (icon != other.icon) return false
    if (descriptionRes != other.descriptionRes) return false
    if (staysDismissed != other.staysDismissed) return false

    return true
  }
}

object SwipeUiModels {

  val None = SwipeUiModel(
    swipeAction = SwipeAction.None,
    icon = Icons.Default.Search,
    descriptionRes = R.string.cancel,
    getColor = { Color.Unspecified },
    staysDismissed = false,
  )

  val Trash = SwipeUiModel(
    swipeAction = SwipeAction.Trash,
    icon = Icons.Default.Delete,
    descriptionRes = R.string.delete,
    getColor = { Color.Red },
    staysDismissed = true,
  )
}
