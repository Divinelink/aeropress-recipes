package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.components.Material3Card
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.FabSize
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
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
    onBeanClicked: (Bean) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = LazyListState(),
) {
    LazyColumn(
        state = state,
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
            items(
                items = beans,
                key = {
                    // key is used and acts a similar way as a DiffUtil for compose.
                    it.id
                }
            ) { bean ->
                BeanListItem(
                    bean = bean,
                    onBeanClicked = { onBeanClicked(bean) }
                )
            }

            item {
                Spacer(Modifier.height(FabSize))
            }
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
fun EmptyBeansScreenPreview() {
    AeropressTheme {
        Surface {
            BeansList(
                beans = listOf(),
                onBeanClicked = {}
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ListBeansScreenPreview() {
    @Suppress("MagicNumber")
    val beans = (1..3).map { index ->
        Bean(
            id = index.toString(),
            name = "Bean name $index",
            roasterName = "Roaster name $index",
            origin = "Origin $index",
            roastLevel = RoastLevel.Dark,
            process = ProcessMethod.Honey,
            rating = index,
            tastingNotes = "",
            additionalNotes = "",
            roastDate = ""
        )
    }.toMutableList()

    AeropressTheme {
        Surface {
            BeansList(
                beans = beans,
                onBeanClicked = {}
            )
        }
    }
}
