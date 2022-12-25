package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldView(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @DrawableRes trailingIconRes: Int? = null,
) {
    var text by remember { mutableStateOf("Test") }

    Column {
        Text(
            text = stringResource(id = titleRes),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onBackground
        )

        TextField(
            shape = RoundedCornerShape(8.dp),
            trailingIcon = { if (trailingIconRes != null) Image(painterResource(trailingIconRes), null) },
            leadingIcon = { if (trailingIconRes != null) Image(painterResource(trailingIconRes), null) },
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(40.dp),
            value = text,
            singleLine = true,
            onValueChange = { text = it },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
    }
}

@Composable
@Preview
fun TextFieldPreview() {
    AeropressTheme {
        TextFieldView(
            titleRes = R.string.Beans__roast_date,
            trailingIconRes = R.drawable.ic_calendar
        )
        TextFieldView(
            titleRes = R.string.Beans__roast_date,
            trailingIconRes = R.drawable.ic_calendar
        )
    }
}

@Composable
@Preview
fun TextFieldWithoutIconPreview() {
    AeropressTheme {
        TextFieldView(
            titleRes = R.string.Beans__bean_name,
        )
    }
}
