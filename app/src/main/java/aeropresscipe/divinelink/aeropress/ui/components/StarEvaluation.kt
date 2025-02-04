@file: Suppress("MagicNumber")

package aeropresscipe.divinelink.aeropress.ui.components

import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val STAR_COUNT = 5

val STAR_COLOR = Color(255, 212, 0)

@Composable
fun StarEvaluation(
  modifier: Modifier = Modifier,
  onValueChange: (Int) -> Unit,
  value: Int,
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center,
  ) {
    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
      for (index in 0 until STAR_COUNT) {
        IconButton(onClick = { onValueChange(index + 1) }) {
          Icon(
            imageVector = Icons.Rounded.Star,
            tint = if (index < value) {
              STAR_COLOR
            } else {
              Color.Gray
            },
            contentDescription = null,
            modifier = Modifier.size(36.dp),
          )
        }
      }
    }
  }
}

@Preview
@Composable
private fun StarEvaluationPreview() {
  AeropressTheme {
    Surface {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        val values = List(5) { remember { mutableIntStateOf(it + 1) } }
        values.forEachIndexed { index, value ->
          StarEvaluation(
            onValueChange = { value.intValue = it },
            value = value.intValue,
          )
        }
      }
    }
  }
}
