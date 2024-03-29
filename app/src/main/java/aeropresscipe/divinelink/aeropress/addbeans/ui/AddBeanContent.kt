@file:Suppress("MagicNumber", "LongMethod")

package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.ui.UIText
import aeropresscipe.divinelink.aeropress.ui.components.CustomOutlinedTextField
import aeropresscipe.divinelink.aeropress.ui.components.DatePicker
import aeropresscipe.divinelink.aeropress.ui.components.SelectOptionField
import aeropresscipe.divinelink.aeropress.ui.components.SimpleAlertDialog
import aeropresscipe.divinelink.aeropress.ui.getString
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import aeropresscipe.divinelink.aeropress.ui.theme.BottomSheetItemShape
import aeropresscipe.divinelink.aeropress.ui.theme.BottomSheetShape
import aeropresscipe.divinelink.aeropress.ui.theme.fadedBackgroundColor
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddBeanContent(
    viewState: AddBeanViewState,
    onBeanNameChanged: (String) -> Unit,
    onRoasterNameChanged: (String) -> Unit,
    onOriginChanged: (String) -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    onOptionSelectedFromBottomSheet: (String) -> Unit,
    onRoastLevelClick: () -> Unit,
    onProcessClick: () -> Unit,
    onSubmitClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    navigateUp: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val coroutineScope = rememberCoroutineScope()
    val deleteBeanDialog = remember { mutableStateOf(false) }

    LaunchedEffect(viewState as? AddBeanViewState.Completed) {
        // Go Back
        if (viewState is AddBeanViewState.Completed) {
            navigateUp()
        }
    }

    if (viewState is AddBeanViewState.ModifyBean &&
        viewState.bottomSheetContent.isNullOrEmpty()
    ) {
        LaunchedEffect(modalBottomSheetState) {
            if (modalBottomSheetState.isVisible) {
                modalBottomSheetState.hide()
            }
        }
    }

    BackHandler(modalBottomSheetState.isVisible) {
        coroutineScope.launch { modalBottomSheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetShape = BottomSheetShape,
        modifier = Modifier.navigationBarsPadding(),
        sheetState = modalBottomSheetState,
        scrimColor = MaterialTheme.colorScheme.fadedBackgroundColor(),
        sheetContent = {
            // As of an issue of ModalBottomSheetLayout,
            // a min size of 1.dp is needed for application not to crash.
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                if (viewState is AddBeanViewState.ModifyBean) {
                    BottomSheetContent(
                        sheetContent = viewState.bottomSheetContent ?: mutableListOf(),
                        selectedOption = viewState.bottomSheetSelectedOption,
                        onOptionSelected = onOptionSelectedFromBottomSheet,
                        title = viewState.bottomSheetTitle,
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = viewState.title.getString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = navigateUp,
                        ) {
                            Icon(Icons.Filled.Close, null)
                        }
                    },
                    actions = {
                        if (viewState.withDeleteAction) {
                            IconButton(
                                onClick = {
                                    deleteBeanDialog.value = true
                                },
                            ) {
                                Icon(Icons.Default.Delete, null)
                            }
                        }
                    }
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 8.dp,
                    end = 8.dp
                )
            ) {

                Spacer(modifier = Modifier.height(12.dp))
                CustomOutlinedTextField(
                    text = viewState.bean.name,
                    onValueChange = onBeanNameChanged,
                    labelText = stringResource(id = R.string.Beans__bean_name),
                    maxLines = 1,
                    isError = (viewState is AddBeanViewState.Error),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )

                Spacer(modifier = Modifier.height(12.dp))
                CustomOutlinedTextField(
                    text = viewState.bean.roasterName,
                    onValueChange = onRoasterNameChanged,
                    labelText = stringResource(id = R.string.Beans__roaster_name),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )

                Spacer(modifier = Modifier.height(12.dp))
                CustomOutlinedTextField(
                    text = viewState.bean.origin,
                    onValueChange = onOriginChanged,
                    labelText = stringResource(id = R.string.Beans__origin),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))
                DatePicker(
                    onValueChange = onDateChanged,
                    value = viewState.bean.roastDate,
                    label = R.string.Beans__roast_date,
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .wrapContentSize(),
                    style = MaterialTheme.typography.titleMedium,
                    text = stringResource(R.string.Beans__details),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(12.dp))
                SelectOptionField(
                    onClick = {
                        onRoastLevelClick()
                        coroutineScope.launch {
                            if (modalBottomSheetState.isVisible) {
                                modalBottomSheetState.hide()
                            } else {
                                modalBottomSheetState.show()
                            }
                        }
                    },
                    onValueChange = {
                        // Intentionally Blank.
                    },
                    label = R.string.Beans__roast_level,
                    value = viewState.bean.roastLevel?.name
                )

                Spacer(modifier = Modifier.height(12.dp))
                SelectOptionField(
                    onClick = {
                        onProcessClick()
                        coroutineScope.launch {
                            if (modalBottomSheetState.isVisible) {
                                modalBottomSheetState.hide()
                            } else {
                                modalBottomSheetState.show()
                            }
                        }
                    },
                    onValueChange = {
                        // Intentionally Blank.
                    },
                    label = R.string.Beans__process,
                    value = viewState.bean.process?.let { processMethod ->
                        stringResource(processMethod.stringRes)
                    }
                )
                Spacer(modifier = Modifier.height(120.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.5F),
                        onClick = { onSubmitClicked() }
                    ) {
                        Text(viewState.submitButtonText.getString())
                    }
                }
            }
            /*        if (viewState is AddBeanViewState.Error &&
                    viewState.error is AddBeanResult.Failure.Unknown
                ) {

                }
                */

            if (deleteBeanDialog.value) {
                SimpleAlertDialog(
                    confirmClick = onDeleteClicked,
                    dismissClick = {
                        deleteBeanDialog.value = false
                    },
                    title = UIText.ResourceText(R.string.Beans__delete_bean),
                    text = UIText.ResourceText(R.string.Beans__delete_bean_message),
                    confirmText = UIText.ResourceText(R.string.delete),
                    dismissText = UIText.ResourceText(R.string.cancel),
                )
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    sheetContent: MutableList<out UIText>,
    selectedOption: UIText?,
    onOptionSelected: (String) -> Unit,
    title: UIText?,
) {
    Column {
        if (title != null) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = title.getString(),
            )
        }
        sheetContent.forEach { uiText ->
            val item = uiText.getString()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(BottomSheetItemShape)
                    .height(48.dp)
                    .fillMaxWidth()
                    .clickable {
                        onOptionSelected(item)
                    }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.9F)
                        .padding(start = 16.dp),
                    text = uiText.getString()
                )

                RadioButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    selected = selectedOption?.getString() == item,
                    onClick = null
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun BeansScreenPreview() {
    AeropressTheme {
        Surface {
            AddBeanContent(
                viewState = AddBeanViewState.Initial,
                onDateChanged = {},
                onRoastLevelClick = {},
                onProcessClick = {},
                onBeanNameChanged = {},
                onRoasterNameChanged = {},
                onOriginChanged = {},
                onOptionSelectedFromBottomSheet = {},
                onSubmitClicked = {},
                onDeleteClicked = {},
                navigateUp = {},
            )
        }
    }
}
