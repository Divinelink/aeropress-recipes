package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.textColorDisabled
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SelectOptionView(
    onClick: () -> Unit,
    @StringRes hint: Int,
) {
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    onClick()
                },
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
            contentColor = MaterialTheme.colorScheme.onBackground,
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    color = MaterialTheme.colorScheme.textColorDisabled(),
                    text = stringResource(hint),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down_24),
                    contentDescription = null,
                    Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
@Preview(name = "Roast Level Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Roast Level Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RoastLevelPreview() {
    AeropressTheme {
        SelectOptionView(
            onClick = { },
            hint = R.string.Beans__roast_level
        )
    }
}
