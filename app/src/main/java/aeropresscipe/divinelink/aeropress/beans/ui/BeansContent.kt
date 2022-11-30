package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.components.Material3CircularProgressIndicator
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

const val ADD_BREW_BUTTON_TAG = "ADD_BREW_BUTTON"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeansContent(
    viewState: BeanTrackerViewState,
    onAddButtonClicked: () -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            AddBeanButton(
                onClick = onAddButtonClicked,
            )
        },
        topBar = {
            //            ToolbarAndDialog(
            //                viewState,
            //                onDateSelected,
            //                onPreviousDateButtonClicked,
            //                onNextDateButtonClicked,
            //            )
        },
    ) { _ ->
        if (viewState is BeanTrackerViewState.Completed) {
            BeansList(
                viewState.beans,
                onBeanClicked = {
                    // Navigate to AddBeanScreen
                },
            )
        }

        if (viewState.showLoading) {
            BeansLoadingContent()
        }
    }
}

@Composable
private fun AddBeanButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .navigationBarsPadding()
            .testTag(ADD_BREW_BUTTON_TAG),
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = stringResource(R.string.Beans__add_bean),
        )
    }
}

@Composable
private fun BeansLoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Material3CircularProgressIndicator(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
        )
    }
}

@Suppress("MagicNumber")
class BeansContentViewStateProvider : PreviewParameterProvider<BeanTrackerViewState> {

    override val values: Sequence<BeanTrackerViewState>
        get() {
            val beans = (1..3).map { index ->
                Bean(
                    id = index.toString(),
                    name = "Bean name $index",
                    roasterName = "Roaster name $index",
                    origin = "Origin $index",
                    roastLevel = RoastLevel.Medium,
                    process = ProcessMethod.Honey,
                    rating = index,
                    tastingNotes = "",
                    additionalNotes = "",
                    roastDate = ""
                )
            }.toMutableList()

            val initialState = BeanTrackerViewState.Initial

            val activeState = BeanTrackerViewState.Active

            val emptyState = BeanTrackerViewState.Completed(
                beans = emptyList()
            )

            val completedState = BeanTrackerViewState.Completed(
                beans = beans
            )

            return sequenceOf(
                initialState,
                activeState,
                emptyState,
                completedState
            )
        }
}

@Preview(
    name = "Night Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "Day Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview
@Suppress("UnusedPrivateMember")
@Composable
private fun BeansContentPreview(
    @PreviewParameter(BeansContentViewStateProvider::class)
    viewState: BeanTrackerViewState,
) {
    AeropressTheme {
        BeansContent(
            viewState = viewState,
            onAddButtonClicked = {},
        )
    }
}
