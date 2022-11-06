package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.ButtonShape
import aeropresscipe.divinelink.aeropress.ui.theme.textColorDisabled
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectOptionView(
    modifier: Modifier = Modifier,
    value: String? = null,
    onClick: () -> Unit,
    @StringRes hint: Int,
    @DrawableRes trailingIcon: Int = R.drawable.ic_arrow_down_24
) {
    val color = if (value.isNullOrEmpty()) {
        MaterialTheme.colorScheme.textColorDisabled()
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val text = if (value.isNullOrEmpty()) {
        stringResource(hint)
    } else {
        value
    }

    Column {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(shape = ButtonShape)
                .clickable {
                    onClick()
                },
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
            contentColor = MaterialTheme.colorScheme.onBackground,
            shape = ButtonShape
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    color = color,
                    style = MaterialTheme.typography.bodyMedium,
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 12.dp),
                )
                Icon(
                    painter = painterResource(id = trailingIcon),
                    contentDescription = null,
                    Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
@Preview(name = "Roast Level Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Roast Level Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RoastLevelPreview() {
    AeropressTheme {
        SelectOptionView(
            onClick = { },
            hint = R.string.Beans__roast_level
        )
    }
}
