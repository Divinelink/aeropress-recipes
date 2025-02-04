@file:Suppress("MagicNumber", "LongMethod")

package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.ui.UIText
import aeropresscipe.divinelink.aeropress.ui.components.CustomOutlinedTextField
import aeropresscipe.divinelink.aeropress.ui.components.DatePicker
import aeropresscipe.divinelink.aeropress.ui.components.SelectOptionField
import aeropresscipe.divinelink.aeropress.ui.components.SimpleAlertDialog
import aeropresscipe.divinelink.aeropress.ui.components.StarEvaluation
import aeropresscipe.divinelink.aeropress.ui.getString
import aeropresscipe.divinelink.aeropress.ui.theme.AeropressTheme
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.divinelink.aeropress.recipes.R
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBeanContent(
  viewState: AddBeanViewState,
  onBeanNameChanged: (String) -> Unit,
  onRoasterNameChanged: (String) -> Unit,
  onOriginChanged: (String) -> Unit,
  onDateChanged: (LocalDate) -> Unit,
  onOptionSelectedFromBottomSheet: (String) -> Unit,
  onRatingChanged: (Int) -> Unit,
  onRoastLevelClick: () -> Unit,
  onProcessClick: () -> Unit,
  onSubmitClicked: () -> Unit,
  onDeleteClicked: () -> Unit,
  navigateUp: () -> Unit,
) {
  val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  val scope = rememberCoroutineScope()
  val deleteBeanDialog = remember { mutableStateOf(false) }

  LaunchedEffect(viewState as? AddBeanViewState.Completed) {
    if (viewState is AddBeanViewState.Completed) {
      navigateUp()
    }
  }

  LaunchedEffect(openBottomSheet) {
    if (openBottomSheet) {
      modalBottomSheetState.show()
    } else {
      modalBottomSheetState.hide()
    }
  }

  BackHandler(modalBottomSheetState.isVisible) {
    scope.launch { modalBottomSheetState.hide() }
  }

  if (openBottomSheet) {
    BeanContentModalBottomSheet(
      sheetState = modalBottomSheetState,
      onDismissRequest = { openBottomSheet = false },
      content = {
        if (viewState is AddBeanViewState.ModifyBean) {
          BottomSheetContent(
            sheetContent = viewState.bottomSheetContent ?: mutableListOf(),
            selectedOption = viewState.bottomSheetSelectedOption,
            onOptionSelected = {
              openBottomSheet = false
              onOptionSelectedFromBottomSheet(it)
            },
            title = viewState.bottomSheetTitle,
          )
        }
      },
    )
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .navigationBarsPadding(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = viewState.title.getString(),
            style = MaterialTheme.typography.titleMedium,
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
        },
      )
    },
    bottomBar = {
      Button(
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        onClick = onSubmitClicked,
        enabled = viewState.isSubmitButtonEnabled,
      ) {
        Text(viewState.submitButtonText.getString())
      }
    },
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(
          top = paddingValues.calculateTopPadding(),
          start = 8.dp,
          end = 8.dp,
          bottom = paddingValues.calculateBottomPadding(),
        ),
    ) {
      Spacer(modifier = Modifier.height(12.dp))
      CustomOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        text = viewState.bean.name,
        onValueChange = onBeanNameChanged,
        labelText = stringResource(id = R.string.Beans__bean_name),
        maxLines = 1,
        isError = (viewState is AddBeanViewState.Error),
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Next,
        ),
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CustomOutlinedTextField(
          modifier = Modifier.fillMaxWidth(0.5f),
          text = viewState.bean.roasterName,
          onValueChange = onRoasterNameChanged,
          labelText = stringResource(id = R.string.Beans__roaster_name),
          maxLines = 1,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
          ),
        )

        CustomOutlinedTextField(
          modifier = Modifier.fillMaxWidth(1f),
          text = viewState.bean.origin,
          onValueChange = onOriginChanged,
          labelText = stringResource(id = R.string.Beans__origin),
          maxLines = 1,
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
          ),
        )
      }
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
          openBottomSheet = true
        },
        onValueChange = {
          // Intentionally Blank.
        },
        label = R.string.Beans__roast_level,
        value = viewState.bean.roastLevel?.name,
      )

      Spacer(modifier = Modifier.height(12.dp))
      SelectOptionField(
        onClick = {
          onProcessClick()
          openBottomSheet = true
        },
        onValueChange = {
          // Intentionally Blank.
        },
        label = R.string.Beans__process,
        value = viewState.bean.process?.let { processMethod ->
          stringResource(processMethod.stringRes)
        },
      )

      Text(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 12.dp)
          .padding(vertical = 8.dp),
        text = stringResource(R.string.Beans__rating),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
      )
      StarEvaluation(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        onValueChange = onRatingChanged,
        value = viewState.bean.rating,
      )
    }

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
        onRatingChanged = {},
        onOriginChanged = {},
        onOptionSelectedFromBottomSheet = {},
        onSubmitClicked = {},
        onDeleteClicked = {},
        navigateUp = {},
      )
    }
  }
}
