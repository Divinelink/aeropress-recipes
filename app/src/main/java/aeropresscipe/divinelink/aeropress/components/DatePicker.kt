package aeropresscipe.divinelink.aeropress.components

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.SelectOptionView
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun DatePicker(
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
        this.datepicker { newDate ->
            // to do
        }
    }

    SelectOptionView(
        onClick = { dialogState.show() },
        hint = hint,
        trailingIcon = trailingIcon
    )
}

@Preview
@Composable
fun SelectDateFieldPreview() {
    AeropressTheme {
        DatePicker(
            hint = R.string.Beans__roast_date,
        )
    }
}
