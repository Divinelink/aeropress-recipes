package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.R
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

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
            if (viewState.isEmpty) {
                // Show Empty State

            } else {
                // Show Beans

            }
        }
    }
}

@Composable
private fun AddBeanButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
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
