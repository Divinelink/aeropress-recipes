package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.ui.components.STAR_COLOR
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun CoffeeBeanCard(
  coffeeBean: Bean,
  onBeanClicked: (Bean) -> Unit,
  modifier: Modifier = Modifier,
) {
  ElevatedCard(
    modifier = modifier.padding(horizontal = 16.dp),
    onClick = { onBeanClicked.invoke(coffeeBean) },
    colors = CardDefaults.cardColors(
      contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ),
    elevation = CardDefaults.elevatedCardElevation(
      defaultElevation = 6.dp,
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth(MAX_COLUMN_WIDTH)
          .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp),
          itemVerticalAlignment = Alignment.CenterVertically,
        ) {
          val annotatedString = buildAnnotatedString {
            withStyle(
              MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                color = MaterialTheme.colorScheme.onSurface,
              ),
            ) {
              append(coffeeBean.name)
            }

            if (coffeeBean.origin.isNotEmpty()) {
              withStyle(
                MaterialTheme.typography.titleSmall.toSpanStyle().copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
              ) {
                append(" • ")
                append(coffeeBean.origin)
              }
            }
          }
          Text(
            text = annotatedString,
            color = MaterialTheme.colorScheme.onSurface,
          )
        }

        if (coffeeBean.roasterName.isNotEmpty()) {
          Text(
            style = MaterialTheme.typography.titleSmall,
            text = coffeeBean.roasterName,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      if (coffeeBean.rating > 0) {
        RatingItem(coffeeBean.rating)
      }
    }
  }
}

@Composable
private fun RatingItem(rating: Int) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(
      style = MaterialTheme.typography.titleSmall,
      text = rating.toString(),
      color = MaterialTheme.colorScheme.onSurface,

    )
    Spacer(modifier = Modifier.width(4.dp))
    Icon(
      imageVector = Icons.Rounded.Star,
      contentDescription = "Rating",
      tint = STAR_COLOR,
    )
  }
}

private const val MAX_COLUMN_WIDTH = 0.9F

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun CoffeeBeanCardPreview(@PreviewParameter(BeanPreviewParameterProvider::class) coffeeBean: Bean) {
  AeropressTheme {
    Surface(modifier = Modifier.padding(8.dp)) {
      CoffeeBeanCard(
        coffeeBean = coffeeBean,
        onBeanClicked = {},
      )
    }
  }
}

internal class BeanPreviewParameterProvider : PreviewParameterProvider<Bean> {
  override val values: Sequence<Bean>
    get() {
      val doubleLetterBean = Bean(
        id = "test",
        name = "Bean name",
        roasterName = "Roaster name",
        origin = "Origin name",
        roastDate = LocalDate.now(),
        roastLevel = RoastLevel.Light,
        process = ProcessMethod.CarbonicMaceration,
        rating = 5,
        tastingNotes = "",
        additionalNotes = "",
        timestamp = "",
      )

      val singleLetterBean = doubleLetterBean.copy(
        name = "Single",
        roastLevel = RoastLevel.Medium,
        origin = "Brazil, Costa Rica, Columbia",
      )

      val multipleLetterBean = doubleLetterBean.copy(
        name = "Omsom Roasters Athens",
        roastLevel = RoastLevel.Medium,
      )

      val largeBeanName = doubleLetterBean.copy(
        name = "Roaster with a very large name that normally would not fit on this space.",
        roastLevel = RoastLevel.Medium,
      )

      return sequenceOf(
        doubleLetterBean,
        singleLetterBean,
        multipleLetterBean,
        largeBeanName,
      )
    }
}
