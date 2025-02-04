@file:Suppress("MagicNumber")

package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.ui.UIText
import aeropresscipe.divinelink.aeropress.ui.getString
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BeanContentModalBottomSheet(
  modifier: Modifier = Modifier,
  sheetState: SheetState,
  onDismissRequest: () -> Unit,
  content: @Composable () -> Unit,
) {
  ModalBottomSheet(
    modifier = modifier,
    sheetState = sheetState,
    onDismissRequest = onDismissRequest,
  ) {
    Column {
      content()
      Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBarsIgnoringVisibility))
    }
  }
}

@Composable
fun BottomSheetContent(
  sheetContent: MutableList<out UIText>,
  selectedOption: UIText?,
  onOptionSelected: (String) -> Unit,
  title: UIText?,
) {
  Column {
    if (title != null) {
      Text(
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = title.getString(),
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))

    sheetContent.forEach { uiText ->
      val item = uiText.getString()
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .height(52.dp)
          .fillMaxWidth()
          .clickable {
            onOptionSelected(item)
          },
      ) {
        Text(
          modifier = Modifier
            .fillMaxWidth(0.9F)
            .padding(start = 16.dp),
          text = uiText.getString(),
        )

        RadioButton(
          modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
          selected = selectedOption?.getString() == item,
          onClick = null,
        )
      }
    }
  }
}
