package aeropresscipe.divinelink.aeropress.ui.components.swipeable

object SwipeActions {
  data class Actions(
    val onNone: () -> Unit,
    val onTrash: () -> Unit,
  )
}
