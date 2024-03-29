package aeropresscipe.divinelink.aeropress.ui.components

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.TextFieldShape
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    enabled: Boolean = true,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = MaterialTheme.colorScheme.onSurface,
    ),
    withTrailingIcon: Boolean = false,
    withLeadingIcon: Boolean = false,
    isError: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column {

        SmallOutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = labelText,
                )
            },
            shape = TextFieldShape,
            modifier = modifier
                .heightIn(dimensionResource(id = R.dimen.text_field_height))
                .fillMaxWidth(),
            enabled = enabled,
            colors = colors,
            withLeadingIcon = withLeadingIcon,
            withTrailingIcon = withTrailingIcon,
            isError = isError,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
        )

        AnimatedVisibility(visible = isError) {
            Text(
                text = stringResource(R.string.empty_field_error_message, labelText),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        start = 16.dp,
                    ),
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ErrorCustomOutlinedTextFieldPreview() {
    AeropressTheme {
        Surface {
            CustomOutlinedTextField(
                text = "Ethiopia",
                onValueChange = {},
                labelText = "Origin",
                isError = true,
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomOutlinedTextFieldPreview() {
    AeropressTheme {
        Surface {
            CustomOutlinedTextField(
                text = "Ethiopia",
                onValueChange = {},
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
                onValueChange = {},
                labelText = "Origin",
            )
        }
    }
}
