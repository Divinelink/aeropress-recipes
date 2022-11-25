package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.components.Material3Card
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.MaterialCardShape
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun BeanListItem(
    bean: Bean,
    onBeanClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Material3Card(
        modifier = modifier
            .clip(MaterialCardShape)
            .clickable {
                onBeanClicked.invoke()
            },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularText(bean.name)
        }
    }
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
 * Extension method that extracts the first two letters of a [String], no matter how long the string.
 */
private fun CharSequence.extraFirstTwoLetters(): String {
    return this.split(" ")
        .take(2)
        .map { it.first() }
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
                roastDate = "12/12/22",
                roastLevel = RoastLevel.Light,
                process = ProcessMethod.CarbonicMaceration,
                rating = 5,
                tastingNotes = "",
                additionalNotes = "",
            )

            val singleLetterBean = doubleLetterBean.copy(
                name = "Single",
                roastLevel = RoastLevel.Medium
            )

            val multipleLetterBean = doubleLetterBean.copy(
                name = "Omsom Roasters Athens",
                roastLevel = RoastLevel.Medium
            )

            return sequenceOf(
                doubleLetterBean,
                singleLetterBean,
                multipleLetterBean
            )
        }
}

@Composable
@Preview(
    name = "Day mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Night mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Suppress("UnusedPrivateMember")
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
