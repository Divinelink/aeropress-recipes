package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.CustomOutlinedTextField
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.HorizontalIconPadding
import aeropresscipe.divinelink.aeropress.ui.theme.TextFieldShape
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SelectOptionField(
    modifier: Modifier = Modifier,
    value: String? = null,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    @DrawableRes trailingIcon: Int = R.drawable.ic_arrow_down_24,
    @DrawableRes leadingIcon: Int? = null,
) {
    Box {
        CustomOutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            text = value ?: "",
            onValueChange = onValueChange,
            labelText = stringResource(id = label),
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium)
            ),
            withTrailingIcon = true,
            withLeadingIcon = leadingIcon != null,
        )

        Box(
            modifier = Modifier
                .clip(TextFieldShape)
                .heightIn(dimensionResource(id = R.dimen.text_field_height))
                .clickable {
                    onClick.invoke()
                }
                .fillMaxWidth()
        ) {
            // Leading Icon
            if (leadingIcon != null) {
                Icon(
                    modifier = Modifier
                        .padding(start = HorizontalIconPadding)
                        .align(Alignment.CenterStart),
                    painter = painterResource(leadingIcon),
                    contentDescription = null
                )
            }
            // Trailing Icon
            Icon(
                modifier = Modifier
                    .padding(end = HorizontalIconPadding)
                    .align(Alignment.CenterEnd),
                painter = painterResource(trailingIcon),
                contentDescription = null
            )
        }
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
                leadingIcon = R.drawable.ic_calendar
            )
        }
    }
}
