package aeropresscipe.divinelink.aeropress.ui.components.swipeable

data class SwipeActionsUiModel(
  val start: SwipeUiModel,
  val end: SwipeUiModel,
) {
  fun atLastOneActionSet() = start.swipeAction != SwipeAction.None ||
    end.swipeAction != SwipeAction.None
}
