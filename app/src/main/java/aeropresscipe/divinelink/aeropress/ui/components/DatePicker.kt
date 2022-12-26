package aeropresscipe.divinelink.aeropress.ui.components

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    value: LocalDate? = null,
    onValueChange: (LocalDate) -> Unit,
    @StringRes label: Int,
    @DrawableRes trailingIcon: Int = R.drawable.ic_calendar,
) {
    val dialogState = rememberMaterialDialogState()
    val selectFieldValue = value?.toUIString() ?: ""

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        }
    ) {
        this.datepicker(
            initialDate = value ?: LocalDate.now(),
            onDateChange = onValueChange
        )
    }

    SelectOptionField(
        modifier = modifier,
        value = selectFieldValue,
        onClick = { dialogState.show() },
        label = label,
        trailingIcon = trailingIcon,
        onValueChange = { onValueChange(value ?: LocalDate.now()) },
    )
}

private fun LocalDate.toUIString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    return formatter.format(this)
}

@Preview(name = "Light Mode - Filled", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode - Filled", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectDateFieldPreview() {
    AeropressTheme {
        Surface {
            DatePicker(
                onValueChange = {},
                label = R.string.Beans__roast_date,
            )
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectDateFieldNoValuePreview() {
    AeropressTheme {
        Surface {
            DatePicker(
                onValueChange = {},
                value = LocalDate.now(),
                label = R.string.Beans__roast_date,
            )
        }
    }
}
