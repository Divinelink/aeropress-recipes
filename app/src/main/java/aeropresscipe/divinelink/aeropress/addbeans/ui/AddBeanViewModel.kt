package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.DeleteBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.UpdateBeanUseCase
import aeropresscipe.divinelink.aeropress.ui.UIText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.aeropress.recipes.R
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(
    private val addBeanUseCase: AddBeanUseCase,
    private val updateBeanUseCase: UpdateBeanUseCase,
    private val deleteBeanUseCase: DeleteBeanUseCase,
) : ViewModel() {

    private val _viewState: MutableStateFlow<AddBeanViewState> =
        MutableStateFlow(AddBeanViewState.Initial)
    val viewState: StateFlow<AddBeanViewState> = _viewState

    fun setBean(bean: Bean?) {
        if (bean == null) {
            _viewState.value = AddBeanViewState.ModifyBean(
                title = UIText.ResourceText(R.string.AddBeans__add_title),
                submitButtonText = UIText.ResourceText(R.string.save),
            )
        } else {
            _viewState.value = AddBeanViewState.ModifyBean(
                bean = bean,
                title = UIText.ResourceText(R.string.AddBeans__update_title),
                submitButtonText = UIText.ResourceText(R.string.update),
                withDeleteAction = true,
            )
        }
    }

    fun onBeanNameChanged(name: String) {
        _viewState.updateBean { currentBean ->
            currentBean.copy(name = name)
        }
    }

    fun onRoasterNameChanged(roasterName: String) {
        _viewState.updateBean { currentBean ->
            currentBean.copy(roasterName = roasterName)
        }
    }

    fun onOriginChanged(origin: String) {
        _viewState.updateBean { currentBean ->
            currentBean.copy(origin = origin)
        }
    }

    fun onDateChanged(date: LocalDate) {
        _viewState.updateBean { currentBean ->
            currentBean.copy(roastDate = date)
        }
    }

    fun onSelectFromBottomSheet(value: String) {
        enumValues<RoastLevel>().find { it.name == value }?.let {
            _viewState.updateBean { currentBean ->
                currentBean.copy(roastLevel = it)
            }
        }

        enumValues<ProcessMethod>().find { it.value == value }?.let {
            _viewState.updateBean { currentBean ->
                currentBean.copy(process = it)
            }
        }
    }

    fun onRoastLevelClicked() {
        val content = RoastLevel.values().map { roastLevel ->
            UIText.StringText(roastLevel.name)
        }.toMutableList()

        _viewState.value = AddBeanViewState.ModifyBean(
            bean = viewState.value.bean,
            title = viewState.value.title,
            submitButtonText = viewState.value.submitButtonText,
            withDeleteAction = viewState.value.withDeleteAction,
            bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_roast_level),
            bottomSheetContent = content,
            bottomSheetSelectedOption = viewState.value.bean.roastLevel?.name?.let {
                UIText.StringText(it)
            },
        )
    }

    fun onProcessClicked() {
        val content = ProcessMethod.entries.map { processMethod ->
            UIText.ResourceText(processMethod.stringRes)
        }.toMutableList()

        _viewState.value = AddBeanViewState.ModifyBean(
            bean = viewState.value.bean,
            title = viewState.value.title,
            submitButtonText = viewState.value.submitButtonText,
            withDeleteAction = viewState.value.withDeleteAction,
            bottomSheetContent = content,
            bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_process_method),
            bottomSheetSelectedOption = viewState.value.bean.process?.stringRes?.let {
                UIText.ResourceText(it)
            },
        )
    }

    fun onDeleteBeanClicked() {
        viewModelScope.launch {
            val result = deleteBeanUseCase(viewState.value.bean)
            _viewState.value = when (result.data) {
                AddBeanResult.Success -> {
                    AddBeanViewState.Completed(
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
                        withDeleteAction = viewState.value.withDeleteAction,
                    )
                }
                else -> {
                    AddBeanViewState.Error(
                        AddBeanResult.Failure.Unknown,
                        bean = viewState.value.bean,
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
                        withDeleteAction = viewState.value.withDeleteAction,
                    )
                }
            }
        }
    }

    fun onSubmitClicked() {
        viewModelScope.launch {
            val result = if (viewState.value.bean.id.isEmpty()) {
                addBeanUseCase(viewState.value.bean.copy(id = UUID.randomUUID().toString()))
            } else {
                updateBeanUseCase(viewState.value.bean)
            }

            _viewState.value = when (result.data) {
                AddBeanResult.Success -> {
                    AddBeanViewState.Completed(
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
                        withDeleteAction = viewState.value.withDeleteAction,
                    )
                }
                AddBeanResult.Failure.EmptyName -> {
                    AddBeanViewState.Error(
                        AddBeanResult.Failure.EmptyName,
                        bean = viewState.value.bean,
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
                        withDeleteAction = viewState.value.withDeleteAction,
                    )
                }
                else -> {
                    AddBeanViewState.Error(
                        AddBeanResult.Failure.Unknown,
                        bean = viewState.value.bean,
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
                        withDeleteAction = viewState.value.withDeleteAction,
                    )
                }
            }
        }
    }
}

/**
 * Helper method to update Flow's [bean] parameter.
 * This is called on every update method that can modify the [bean]'s property.
 * This method always return [AddBeanViewState.InsertBean] State if the user tries to update and current state is [AddBeanViewState.Initial].
 *
 * @param [newBean] A lambda function that takes as an argument the current state's value of the bean property
 * and returns the updated bean value.
 */
private fun MutableStateFlow<AddBeanViewState>.updateBean(
    newBean: (Bean) -> Bean,
) {
    when (this.value) {
        is AddBeanViewState.Initial,
        is AddBeanViewState.ModifyBean,
        is AddBeanViewState.Error,
        -> {
            this.value = AddBeanViewState.ModifyBean(
                newBean.invoke(this.value.bean),
                title = this.value.title,
                submitButtonText = this.value.submitButtonText,
                openProcessMethodDrawer = this.value.openProcessMethodDrawer,
                openRoastLevelDrawer = this.value.openRoastLevelDrawer,
                withDeleteAction = this.value.withDeleteAction,
            )
        }
        is AddBeanViewState.Completed -> {
            // Intentionally Blank.
        }
    }
}
