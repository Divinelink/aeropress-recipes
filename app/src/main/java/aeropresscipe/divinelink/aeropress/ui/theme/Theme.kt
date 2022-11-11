package aeropresscipe.divinelink.aeropress.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AeropressTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }

    // Used to converse Material 2 to Material 3 when needed.
    androidx.compose.material.MaterialTheme(
        typography = MD2Typography,
        colors = if (useDarkTheme) {
            DarkMD2Colors
        } else {
            LightMD2Colors
        }
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = AppTypography,
            content = content
        )
    }
}

@Composable
fun ColorScheme.textColorDisabled(): Color {
    return MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
}