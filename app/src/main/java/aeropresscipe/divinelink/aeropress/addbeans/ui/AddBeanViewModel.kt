package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.UpdateBeanUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
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
) : ViewModel() {

    private val _viewState: MutableStateFlow<AddBeanViewState> =
        MutableStateFlow(AddBeanViewState.Initial)
    val viewState: StateFlow<AddBeanViewState> = _viewState

    fun setBean(bean: Bean?) {
        if (bean != null) {
            _viewState.value = AddBeanViewState.UpdateBean(
                bean = bean,
            )
        } else {
            _viewState.value = AddBeanViewState.InsertBean()
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
        // τοδο
    }

    fun onProcessClicked() {
        // τοδο
    }

    fun onSubmitClicked() {
        viewModelScope.launch {
            val result = if (viewState.value.bean.id.isEmpty()) {
                addBeanUseCase(viewState.value.bean.copy(id = UUID.randomUUID().toString()))
            } else {
                updateBeanUseCase(viewState.value.bean)
            }

            if (result is Result.Success) {
                // Update the view state to success state.
                _viewState.value = AddBeanViewState.Completed
            } else if (result is Result.Error) {
                // Update the view state to error state.
                _viewState.value = AddBeanViewState.Error(
                    bean = viewState.value.bean,
                )
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
        is AddBeanViewState.InsertBean,
        -> {
            this.value = AddBeanViewState.InsertBean(
                newBean.invoke(this.value.bean)
            )
        }
        is AddBeanViewState.UpdateBean -> {
            this.value = AddBeanViewState.UpdateBean(
                newBean.invoke(this.value.bean)
            )
        }
        is AddBeanViewState.Completed,
        is AddBeanViewState.Error,
        -> {
            // Intentionally Blank.
        }
    }
}
