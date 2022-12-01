@file:Suppress("MagicNumber")

package aeropresscipe.divinelink.aeropress.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import kotlin.math.roundToInt

//
// @Composable
// fun ExtendableFloatingActionButton(
//    modifier: Modifier = Modifier,
//    extended: Boolean,
//    text: @Composable () -> Unit,
//    icon: @Composable () -> Unit,
//    onClick: () -> Unit = {},
// ) {
//    FloatingActionButton(
//        modifier = modifier,
//        onClick = onClick,
//    ) {
//        Row(
//            modifier = Modifier.padding(
//                start = PaddingSize,
//                end = PaddingSize
//            ),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            icon()
//
//            AnimatedVisibility(visible = extended) {
//                Row {
//                    Spacer(Modifier.width(12.dp))
//                    text()
//                }
//            }
//        }
//    }
// }
//
// private val PaddingSize = 16.dp

/**
 * A layout that shows an icon and a text element used as the content for a FAB that extends with
 * an animation.
 */
@Composable
fun AnimatingFabContent(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    extended: Boolean = true,
) {
    val currentState = if (extended) ExpandableFabStates.Extended else ExpandableFabStates.Collapsed
    val transition = updateTransition(currentState, "fab_transition")

    val textOpacity by transition.animateFloat(
        transitionSpec = {
            if (targetState == ExpandableFabStates.Collapsed) {
                tween(
                    easing = LinearEasing,
                    durationMillis = (TRANSITION_DURATION / 12f * 5).roundToInt() // 5 / 12 frames
                )
            } else {
                tween(
                    easing = LinearEasing,
                    delayMillis = (TRANSITION_DURATION / 3f).roundToInt(), // 4 / 12 frames
                    durationMillis = (TRANSITION_DURATION / 12f * 5).roundToInt() // 5 / 12 frames
                )
            }
        },
        label = "fab_text_opacity"
    ) { state ->
        if (state == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }
    val fabWidthFactor by transition.animateFloat(
        transitionSpec = {
            tween(
                easing = FastOutSlowInEasing,
                durationMillis = TRANSITION_DURATION
            )
            //            if (targetState == ExpandableFabStates.Collapsed) {
            //                tween(
            //                    easing = FastOutSlowInEasing,
            //                    durationMillis = TransitionDuration
            //                )
            //            } else {
            //                tween(
            //                    easing = FastOutSlowInEasing,
            //                    durationMillis = TransitionDuration
            //                )
            //            }
        },
        label = "fab_width_factor"
    ) { state ->
        if (state == ExpandableFabStates.Collapsed) {
            0f
        } else {
            1f
        }
    }
    // Deferring reads using lambdas instead of Floats here can improve performance,
    // preventing recompositions.
    IconAndTextRow(
        icon,
        text,
        { textOpacity },
        { fabWidthFactor },
        modifier = modifier
    )
}

@Composable
private fun IconAndTextRow(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    opacityProgress: () -> Float, // Lambdas instead of Floats, to defer read
    widthProgress: () -> Float,
    modifier: Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            icon()
            Box(modifier = Modifier.graphicsLayer { alpha = opacityProgress() }) {
                text()
            }
        }
    ) { measurables, constraints ->

        val iconPlaceable = measurables[0].measure(constraints)
        val textPlaceable = measurables[1].measure(constraints)

        val height = constraints.maxHeight

        // FAB has an aspect ratio of 1 so the initial width is the height
        val initialWidth = height.toFloat()

        // Use it to get the padding
        val iconPadding = (initialWidth - iconPlaceable.width) / 2f

        // The full width will be : padding + icon + padding + text + padding
        val expandedWidth = iconPlaceable.width + textPlaceable.width + iconPadding * 3

        // Apply the animation factor to go from initialWidth to fullWidth
        val width = androidx.compose.ui.util.lerp(initialWidth, expandedWidth, widthProgress())

        layout(width.roundToInt(), height) {
            iconPlaceable.place(
                iconPadding.roundToInt(),
                constraints.maxHeight / 2 - iconPlaceable.height / 2
            )
            textPlaceable.place(
                (iconPlaceable.width + iconPadding * 2).roundToInt(),
                constraints.maxHeight / 2 - textPlaceable.height / 2
            )
        }
    }
}

private enum class ExpandableFabStates { Collapsed, Extended }

private const val TRANSITION_DURATION = 200
