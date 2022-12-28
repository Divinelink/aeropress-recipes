package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.addbeans.ui.AddBeanActivity
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.ui.components.ExtendableFloatingActionButton
import aeropresscipe.divinelink.aeropress.ui.components.Material3CircularProgressIndicator
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.FabSize
import aeropresscipe.divinelink.aeropress.ui.theme.topBarColor
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate

const val ADD_BREW_BUTTON_TAG = "ADD_BREW_BUTTON"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun BeansContent(
    viewState: BeanTrackerViewState,
    onAddButtonClicked: () -> Unit,
    onBeanClicked: (Bean) -> Unit,
    onAddBeanOpened: () -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    var fabExtended by remember { mutableStateOf(true) }
    val scrollColors = TopAppBarDefaults.smallTopAppBarColors(
        scrolledContainerColor = topBarColor(),
    )
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(scrollState) {
        var prev = 0
        snapshotFlow { scrollState.firstVisibleItemIndex }
            .collect {
                fabExtended = it <= prev
                prev = it
            }
    }

    val context = LocalContext.current
    LaunchedEffect(viewState as? BeanTrackerViewState.Completed) {
        if ((viewState as? BeanTrackerViewState.Completed)?.goToAddBean == true) {
            context.startActivity(
                AddBeanActivity.newIntent(
                    context = context,
                    bean = viewState.selectedBean
                )
            )
            onAddBeanOpened()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(
            left = 0.dp,
            top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding().value.dp,
            right = 0.dp,
            bottom = 0.dp,
            ),
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            AddBeanButton(
                modifier = Modifier.padding(vertical = bottomPadding),
                onClick = onAddButtonClicked,
                extended = fabExtended
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.Beans__bean_tracker),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        /* Do Something*/
                    }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = scrollColors,
            )
        },
    ) { paddingValues ->
        if (viewState is BeanTrackerViewState.Completed) {
            BeansList(
                viewState.beans,
                modifier = modifier
                    .padding(top = paddingValues.calculateTopPadding()),
                // Navigate to AddBeanScreen,
                onBeanClicked = onBeanClicked,
                state = scrollState,
                bottomPadding = bottomPadding,
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
    ExtendableFloatingActionButton(
        modifier = modifier
            .height(FabSize)
            .widthIn(min = FabSize)
            .testTag(ADD_BREW_BUTTON_TAG),
        extended = extended,
        text = {
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(
                    R.string.Beans__add_bean
                ),
            )
        },
        icon = {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(R.string.Beans__add_bean),
            )
        },
        onClick = onClick
    )
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
                    roastDate = LocalDate.now(),
                )
            }.toMutableList()

            val initialState = BeanTrackerViewState.Initial

            val activeState = BeanTrackerViewState.Active()

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
            onBeanClicked = {},
            onAddBeanOpened = {},
            bottomPadding = 0.dp,
        )
    }
}
