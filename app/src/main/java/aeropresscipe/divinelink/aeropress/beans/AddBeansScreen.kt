package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.CustomTextField
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddBeansScreen(
    onRoastLevelClick: () -> Unit,
    onProcessClick: () -> Unit,
) {

    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {

        CustomTextField(
            modifier = Modifier.padding(top = 8.dp),
            placeHolderRes = R.string.Beans__bean_name
        )

        CustomTextField(
            modifier = Modifier.padding(top = 8.dp),
            placeHolderRes = R.string.Beans__roaster_name
        )

        CustomTextField(
            modifier = Modifier.padding(top = 8.dp),
            placeHolderRes = R.string.Beans__origin
        )

        CustomTextField(
            modifier = Modifier.padding(top = 8.dp),
            placeHolderRes = R.string.Beans__roast_date,
            trailingIconRes = R.drawable.ic_calendar
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 12.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.Beans__details),
            color = MaterialTheme.colorScheme.onSurface,
        )

        SelectOptionView(
            modifier = Modifier.padding(bottom = 8.dp),
            onClick = onRoastLevelClick,
            hint = R.string.Beans__roast_level
        )

        SelectOptionView(
            modifier = Modifier.padding(bottom = 8.dp),
            onClick = onProcessClick,
            hint = R.string.Beans__process
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BeansScreenPreview() {
    AeropressTheme {

        AddBeansScreen(
            onRoastLevelClick = {},
            onProcessClick = {}
        )
    }
}
