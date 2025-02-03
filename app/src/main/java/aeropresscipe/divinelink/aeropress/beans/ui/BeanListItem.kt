package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.ui.components.STAR_COLOR
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun BeanListItem(
  bean: Bean,
  onBeanClicked: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    onClick = onBeanClicked,
    elevation = CardDefaults.elevatedCardElevation()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CircularText(bean.name)

      Column(
        modifier = Modifier
          .fillMaxWidth(MAX_COLUMN_WIDTH)
          .padding(start = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {

        Text(
          style = MaterialTheme.typography.bodyLarge,
          text = bean.name,
          color = MaterialTheme.colorScheme.onSurface
        )

        if (bean.roasterName.isNotEmpty()) {
          Text(
            style = MaterialTheme.typography.bodyMedium,
            text = bean.roasterName,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
      ) {
        if (bean.rating > 0) {
          RatingItem(bean.rating)
        }

        val origin = bean.origin.splitToLines()
        if (origin.isNotEmpty()) {
          Text(
            style = MaterialTheme.typography.bodySmall,
            text = origin,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
          )
        }
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
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.width(4.dp))
    Icon(
      imageVector = Icons.Rounded.Star,
      contentDescription = "Rating",
      tint = STAR_COLOR
    )
  }
}

private fun CharSequence.splitToLines(): String {
  return this.split(", ", ",")
    .joinToString("\n")
}

@Composable
private fun CircularText(
  name: String,
) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .width(48.dp)
      .height(48.dp)
      .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
  ) {
    Text(
      text = name.extraFirstTwoLetters(),
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      modifier = Modifier.padding(4.dp),
    )
  }
}

/**
 * Extension method that extracts the first[] two letters of a [String], no matter how long the string.
 */
private fun CharSequence.extraFirstTwoLetters(): String {
  return this.split(" ")
    .take(2)
    .map { it.firstOrNull() ?: "" }
    .joinToString("") { it.toString() }
    .uppercase()
}

private class BeanPreviewParameterProvider : PreviewParameterProvider<Bean> {
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
        largeBeanName
      )
    }
}

private const val MAX_COLUMN_WIDTH = 0.7F

@Composable
@Preview(
  name = "Day mode",
  uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
  name = "Night mode",
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun BeanListItemPreview(
  @PreviewParameter(BeanPreviewParameterProvider::class)
  bean: Bean,
) {
  AeropressTheme {
    Surface(modifier = Modifier.padding(8.dp)) {
      BeanListItem(
        bean,
        onBeanClicked = {}
      )
    }
  }
}
