package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.GroupedCoffeeBeans
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.FabSize
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.divinelink.aeropress.recipes.R
import java.time.LocalDate

@Composable
fun BeansList(
  coffeeBeans: GroupedCoffeeBeans,
  onBeanClicked: (Bean) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  bottomPadding: Dp = 0.dp,
) {
  LazyColumn(
    state = state,
    contentPadding = PaddingValues(bottom = FabSize),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier,
  ) {
    if (coffeeBeans.byDate.values.isEmpty()) {
      item {
        EmptySectionCard(
          text = stringResource(id = R.string.Beans__empty_bean_list_label),
        )
      }
    } else {
      coffeeBeans.byDate.forEach { (date, beans) ->
        stickyHeader {
          Text(
            text = date,
            modifier = Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.surface)
              .padding(8.dp),
            style = MaterialTheme.typography.titleMedium,
          )
        }

        items(
          items = beans,
          key = { it.id },
        ) { coffeeBean ->
          CoffeeBeanCard(
            coffeeBean = coffeeBean,
            onBeanClicked = onBeanClicked,
          )
        }
      }

      item {
        Spacer(Modifier.height(FabSize + bottomPadding))
      }
    }
  }
}

@Composable
private fun EmptySectionCard(
  text: String,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
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
        coffeeBeans = GroupedCoffeeBeans(emptyMap()),
        onBeanClicked = {},
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
      roastDate = LocalDate.now(),
      timestamp = "",
    )
  }.toMutableList()

  val group = beans.groupBy { it.timestamp }

  AeropressTheme {
    Surface {
      BeansList(
        coffeeBeans = GroupedCoffeeBeans(group),
        onBeanClicked = {},
      )
    }
  }
}
