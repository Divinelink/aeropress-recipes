package aeropresscipe.divinelink.aeropress.components

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.TextFieldShape
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    labelText: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
    ),
) {
    SmallOutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        label = {
            Text(
                text = labelText,
            )
        },
        shape = TextFieldShape,
        modifier = modifier
            .heightIn(dimensionResource(id = R.dimen.text_field_height))
            .fillMaxWidth(),
        trailingIcon = trailingIcon,
        enabled = enabled,
        colors = colors
    )
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomOutlinedTextFieldPreview() {
    AeropressTheme {
        Surface {
            CustomOutlinedTextField(
                text = "Ethiopia",
                onTextChanged = {},
                labelText = "Origin",
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomOutlinedTextFieldEmptyPreview() {
    AeropressTheme {
        Surface {
            CustomOutlinedTextField(
                text = "",
                onTextChanged = {},
                labelText = "Origin",
            )
        }
    }
}
