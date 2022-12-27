package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.UpdateBeanUseCase
import aeropresscipe.divinelink.aeropress.ui.UIText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(
    private val addBeanUseCase: AddBeanUseCase,
    private val updateBeanUseCase: UpdateBeanUseCase,
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

    fun onRoastLevelChanged(roastLevel: String) {
        _viewState.updateBean { currentBean ->
            currentBean.copy(roastLevel = enumValues<RoastLevel>().find { it.name == roastLevel })
        }
    }

    fun onProcessChanged(process: String) {
        _viewState.updateBean { currentBean ->
            currentBean.copy(process = ProcessMethod.valueOf(process))
        }
    }

    fun onRoastLevelClicked() {
        _viewState.update { viewState ->
            viewState.copy(
                openRoastLevelDrawer = true
            )
        }
    }

    fun onProcessClicked() {
        _viewState.update { viewState ->
            viewState.copy(
                openProcessMethodDrawer = true
            )
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
                    )
                }
                AddBeanResult.Failure.EmptyName -> {
                    AddBeanViewState.Error(
                        AddBeanResult.Failure.EmptyName,
                        bean = viewState.value.bean,
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
                    )
                }
                else -> {
                    AddBeanViewState.Error(
                        AddBeanResult.Failure.Unknown,
                        bean = viewState.value.bean,
                        submitButtonText = viewState.value.submitButtonText,
                        title = viewState.value.title,
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
            )
        }
        is AddBeanViewState.Completed -> {
            // Intentionally Blank.
        }
    }
}

/**
 * A helper method that enabled us to update the desired properties on the current ViewState.
 * This is needed since Sealed Classes doesn't have a copy method.
 */
private fun AddBeanViewState.copy(
    bean: Bean? = null,
    openProcessMethodDrawer: Boolean? = null,
    openRoastLevelDrawer: Boolean? = null,
): AddBeanViewState {
    return when (this) {
        is AddBeanViewState.Initial -> AddBeanViewState.Initial
        is AddBeanViewState.Error -> AddBeanViewState.Error(
            bean = this.bean,
            submitButtonText = this.submitButtonText,
            title = this.title,
            error = error,
        )
        is AddBeanViewState.Completed -> AddBeanViewState.Completed(
            submitButtonText = this.submitButtonText,
            title = this.title
        )
        is AddBeanViewState.ModifyBean -> AddBeanViewState.ModifyBean(
            bean = bean ?: this.bean,
            openProcessMethodDrawer = openProcessMethodDrawer ?: this.openProcessMethodDrawer,
            openRoastLevelDrawer = openRoastLevelDrawer ?: this.openRoastLevelDrawer,
            title = this.title,
            submitButtonText = this.submitButtonText,
        )
    }
}
