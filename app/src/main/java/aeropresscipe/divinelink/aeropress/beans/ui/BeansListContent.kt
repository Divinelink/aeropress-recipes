package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.components.Material3Card
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BeansList(
    beans: List<Bean>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.list_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.list_padding)),
        modifier = modifier
    ) {
        if (beans.isEmpty()) {
            item {
                EmptySectionCard(
                    text = stringResource(id = R.string.Beans__empty_bean_list_label)
                )
            }
        } else {

        }
    }
}

@Composable
private fun EmptySectionCard(
    text: String,
    modifier: Modifier = Modifier,
) {
    Material3Card(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    vertical = 32.dp,
                    horizontal = 24.dp,
                ),
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BeansScreenPreview() {
    AeropressTheme {
        Surface {
            BeansList(
                beans = listOf(),
            )
        }
    }
}
