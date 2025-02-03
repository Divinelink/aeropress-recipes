package aeropresscipe.divinelink.aeropress.ui.components

import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.HorizontalIconPadding
import aeropresscipe.divinelink.aeropress.ui.theme.TextFieldShape
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.divinelink.aeropress.recipes.R

@Composable
fun SelectOptionField(
  modifier: Modifier = Modifier,
  value: String? = null,
  onClick: () -> Unit,
  onValueChange: (String) -> Unit,
  @StringRes label: Int,
  @DrawableRes trailingIcon: Int = R.drawable.ic_arrow_down_24,
) {
  Box {
    CustomOutlinedTextField(
      modifier = modifier.fillMaxWidth(),
      text = value ?: "",
      onValueChange = onValueChange,
      labelText = stringResource(id = label),
      enabled = true,
      trailingIcon = {
        Icon(
          modifier = Modifier
            .padding(end = HorizontalIconPadding)
            .align(Alignment.CenterEnd),
          painter = painterResource(trailingIcon),
          contentDescription = null
        )
      },
    )

    Spacer(
      Modifier
        .offset(y = 8.dp)
        .clip(TextFieldShape)
        .clickable(onClick = onClick)
        .height(56.dp)
        .fillMaxWidth()
    )
  }
}

@Composable
@Preview(name = "Roast Level Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Roast Level Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RoastLevelPreview() {
  AeropressTheme {
    Surface {
      SelectOptionField(
        onClick = { },
        onValueChange = { },
        label = R.string.Beans__roast_level,
        trailingIcon = R.drawable.ic_calendar
      )
    }
  }
}
