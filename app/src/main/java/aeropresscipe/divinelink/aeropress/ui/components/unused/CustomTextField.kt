@file:Suppress("LongMethod")
package aeropresscipe.divinelink.aeropress.components

import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.divinelink.aeropress.recipes.R

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
//    leadingIcon: (@Composable () -> Unit)? = null,
//    trailingIcon: (@Composable () -> Unit)? = null,
    @DrawableRes leadingIconRes: Int? = null,
    @DrawableRes trailingIconRes: Int? = null,
    @StringRes titleRes: Int? = null,
    @StringRes placeHolderRes: Int? = null,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val leadingIconPadding = if (leadingIconRes == null) 0.dp else 12.dp
    val trailingIconPadding = if (trailingIconRes == null) 0.dp else 12.dp

    Column {
        // Only show Title Text if titleRes != null
        if (titleRes != null) {
            Text(
                text = stringResource(id = titleRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Surface(
            modifier = modifier
                .defaultMinSize(minHeight = 40.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
        ) {
            BasicTextField(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.shapes.small,
                    )
                    .fillMaxWidth(),
                value = text,
                onValueChange = {
                    text = it
                },
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = fontSize
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .padding(start = leadingIconPadding, end = trailingIconPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (leadingIconRes != null) Image(painterResource(leadingIconRes), null)
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            // Only show placeholder is it's not null by default
                            if (placeHolderRes != null && text.isEmpty()) {
                                Text(
                                    stringResource(placeHolderRes),
                                    style = LocalTextStyle.current.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                        fontSize = fontSize
                                    )
                                )
                            }
                            innerTextField()
                        }
                        if (trailingIconRes != null) Image(painterResource(trailingIconRes), null)
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun CustomTextFieldPreview() {
    AeropressTheme {
        CustomTextField(
            titleRes = R.string.Beans__origin,
            placeHolderRes = R.string.Beans__origin_placeholder,
            trailingIconRes = R.drawable.ic_calendar
        )
    }
}
