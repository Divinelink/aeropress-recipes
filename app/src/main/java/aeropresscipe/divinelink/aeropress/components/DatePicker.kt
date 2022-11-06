package aeropresscipe.divinelink.aeropress.components

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.SelectOptionView
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    value: LocalDate? = null,
    onValueChanged: (LocalDate) -> Unit,
    @StringRes hint: Int,
    @DrawableRes trailingIcon: Int = R.drawable.ic_calendar,
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        }
    ) {
        this.datepicker(onDateChange = onValueChanged)
    }

    SelectOptionView(
        modifier = modifier,
        value = value?.toString(),
        onClick = { dialogState.show() },
        hint = hint,
        trailingIcon = trailingIcon
    )
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectDateFieldPreview() {
    AeropressTheme {
        DatePicker(
            value = LocalDate.now(),
            onValueChanged = {},
            hint = R.string.Beans__roast_date,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SelectDateFieldNoValuePreview() {
    AeropressTheme {
        DatePicker(
            onValueChanged = {},
            hint = R.string.Beans__roast_date,
            modifier = Modifier.padding(16.dp)
        )
    }
}
