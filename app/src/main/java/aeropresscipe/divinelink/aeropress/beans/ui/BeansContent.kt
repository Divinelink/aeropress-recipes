package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.components.AnimatingFabContent
import aeropresscipe.divinelink.aeropress.components.Material3CircularProgressIndicator
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

const val ADD_BREW_BUTTON_TAG = "ADD_BREW_BUTTON"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeansContent(
    viewState: BeanTrackerViewState,
    onAddButtonClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var fabExtended by remember { mutableStateOf(true) }

    LaunchedEffect(scrollState) {
        var prev = 0
        snapshotFlow { scrollState.value }
            .collect {
                fabExtended = it <= prev
                prev = it
            }
    }

    Scaffold(
        floatingActionButton = {
            AddBeanButton(
                onClick = onAddButtonClicked,
                extended = fabExtended
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.Beans__bean_tracker),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        /* Do Something*/
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                // scrollBehavior = TopAppBarScrollBehavior()
            )
        },
    ) { paddingValues ->
        if (viewState is BeanTrackerViewState.Completed) {
            BeansList(
                viewState.beans,
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .height(1200.dp) // Todo fix hardcoded height
                    .navigationBarsPadding()
                    .padding(paddingValues),
                onBeanClicked = {
                    // Navigate to AddBeanScreen
                },
            )
        }
    }

    if (viewState.showLoading) {
        BeansLoadingContent()
    }
}

@Composable
private fun AddBeanButton(
    onClick: () -> Unit,
    extended: Boolean,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .widthIn(min = 56.dp)
            .navigationBarsPadding()
            .testTag(ADD_BREW_BUTTON_TAG),
    ) {
        AnimatingFabContent(
            icon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.Beans__add_bean),
                )
            },
            text = {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(
                        R.string.Beans__add_bean
                    ),
                )
            },
            extended = extended
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
            val beans = (1..12).map { index ->
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
