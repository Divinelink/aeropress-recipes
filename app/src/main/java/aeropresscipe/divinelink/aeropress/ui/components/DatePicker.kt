package aeropresscipe.divinelink.aeropress.ui.components

import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.divinelink.aeropress.recipes.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
  modifier: Modifier = Modifier,
  value: LocalDate? = null,
  onValueChange: (LocalDate) -> Unit,
  @StringRes label: Int,
  @DrawableRes trailingIcon: Int = R.drawable.ic_calendar,
) {
  var showDatePicker by remember { mutableStateOf(false) }

  val initialDate = value ?: LocalDate.now()

  val datePickerState = rememberDatePickerState()

  val selectFieldValue = value?.toUIString() ?: ""

  if (showDatePicker) {
    DatePickerDialog(
      onDismissRequest = { showDatePicker = false },
      confirmButton = {
        TextButton(
          onClick = {
            val selectedLocalDate = datePickerState.selectedDateMillis?.let { selectedMillis ->
              Instant.ofEpochMilli(selectedMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            } ?: initialDate

            showDatePicker = false
            onValueChange(selectedLocalDate)
          },
        ) {
          Text(stringResource(R.string.ok))
        }
      },
      dismissButton = {
        TextButton(onClick = { showDatePicker = false }) {
          Text(stringResource(R.string.cancel))
        }
      },
    ) {
      DatePicker(state = datePickerState)
    }
  }

  SelectOptionField(
    modifier = modifier,
    value = selectFieldValue,
    onClick = {
      showDatePicker = true
      datePickerState.selectedDateMillis = value?.atStartOfDay()
        ?.toInstant(ZoneOffset.UTC)
        ?.toEpochMilli()
    },
    label = label,
    trailingIcon = trailingIcon,
    onValueChange = { /* No-op, as clicking should open date picker */ },
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
