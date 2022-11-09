package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.CustomOutlinedTextField
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.TextFieldShape
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SelectOptionView(
    modifier: Modifier = Modifier,
    value: String? = null,
    onClick: () -> Unit,
    @StringRes hint: Int,
    @DrawableRes trailingIcon: Int = R.drawable.ic_arrow_down_24,
) {
    val text = if (value.isNullOrEmpty()) {
        stringResource(hint)
    } else {
        value
    }

    Box {
        CustomOutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            text = "",
            onTextChanged = { },
            labelText = text,
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium)
            )
        )
        Box(
            modifier = Modifier
                .clip(TextFieldShape)
                .heightIn(dimensionResource(id = R.dimen.text_field_height))
                .clickable {
                    onClick.invoke()
                }
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
            SelectOptionView(
                onClick = { },
                hint = R.string.Beans__roast_level
            )
        }
    }
}
