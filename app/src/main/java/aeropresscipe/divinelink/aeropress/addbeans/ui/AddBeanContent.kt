package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.SelectOptionField
import aeropresscipe.divinelink.aeropress.components.CustomOutlinedTextField
import aeropresscipe.divinelink.aeropress.components.DatePicker
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun AddBeanContent(
    viewState: AddBeanViewState,
    onBeanNameChanged: (String) -> Unit,
    onRoasterNameChanged: (String) -> Unit,
    onOriginChanged: (String) -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    onRoastLevelChanged: (String) -> Unit,
    onProcessChanged: (String) -> Unit,
    onRoastLevelClick: () -> Unit,
    onProcessClick: () -> Unit,
) {

    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedTextField(
            text = viewState.bean.name,
            onValueChange = onBeanNameChanged,
            labelText = stringResource(id = R.string.Beans__bean_name),
        )

        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedTextField(
            text = viewState.bean.roasterName,
            onValueChange = onRoasterNameChanged,
            labelText = stringResource(id = R.string.Beans__roaster_name)
        )

        Spacer(modifier = Modifier.height(12.dp))
        CustomOutlinedTextField(
            text = viewState.bean.origin,
            onValueChange = onOriginChanged,
            labelText = stringResource(id = R.string.Beans__origin)
        )
        Spacer(modifier = Modifier.height(12.dp))
        DatePicker(
            onValueChange = onDateChanged,
            label = R.string.Beans__roast_date
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .wrapContentSize(),
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.Beans__details),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))
        SelectOptionField(
            onClick = onRoastLevelClick,
            onValueChange = onRoastLevelChanged,
            label = R.string.Beans__roast_level
        )

        Spacer(modifier = Modifier.height(12.dp))
        SelectOptionField(
            onClick = onProcessClick,
            onValueChange = onProcessChanged,
            label = R.string.Beans__process
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BeansScreenPreview() {
    AeropressTheme {
        Surface {
            AddBeanContent(
                viewState = AddBeanViewState(),
                onDateChanged = {},
                onRoastLevelClick = {},
                onProcessClick = {},
                onBeanNameChanged = {},
                onRoasterNameChanged = {},
                onOriginChanged = {},
                onRoastLevelChanged = {},
                onProcessChanged = {},
            )
        }
    }
}
