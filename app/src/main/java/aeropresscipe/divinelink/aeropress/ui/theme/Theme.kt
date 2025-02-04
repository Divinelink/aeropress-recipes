package aeropresscipe.divinelink.aeropress.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

  MaterialTheme(
    colorScheme = colors,
    typography = AppTypography,
    content = content,
  )
}

@Composable
fun ColorScheme.textColorDisabled(): Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

@Composable
fun ColorScheme.fadedBackgroundColor(): Color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.62f)

@Composable
fun topBarColor(): Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.30f)

val FabSize = 56.dp
