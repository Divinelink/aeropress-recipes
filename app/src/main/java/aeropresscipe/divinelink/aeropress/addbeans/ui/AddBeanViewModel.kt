package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.beans.domain.model.toProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchBeanUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(
    private val fetchBeanUseCase: FetchBeanUseCase,
    private val addBeanUseCase: AddBeanUseCase,
) : ViewModel() {

    private val _viewState: MutableStateFlow<AddBeanViewState> =
        MutableStateFlow(AddBeanViewState())
    val viewState: StateFlow<AddBeanViewState> = _viewState

    fun getBean(bean: Bean) {
        viewModelScope.launch {
            fetchBeanUseCase(bean)
        }
    }

    fun setBean(bean: Bean?) {
        if (bean != null) {
            _viewState.update { viewState ->
                viewState.copy(
                    bean = bean,
                    showLoading = false,
                )
            }
        }
    }

    fun onBeanNameChanged(name: String) {
        _viewState.update { viewState ->
            viewState.copy(
                bean = viewState.bean.copy(name = name),
                showLoading = false,
            )
        }
    }

    fun onRoasterNameChanged(roasterName: String) {
        _viewState.update { viewState ->
            viewState.copy(
                bean = viewState.bean.copy(roasterName = roasterName),
                showLoading = false,
            )
        }
    }

    fun onOriginChanged(origin: String) {
        _viewState.update { viewState ->
            viewState.copy(
                bean = viewState.bean.copy(origin = origin),
                showLoading = false,
            )
        }
    }

    fun onDateChanged(date: LocalDate) {
        _viewState.update { viewState ->
            viewState.copy(
                bean = viewState.bean.copy(roastDate = date.toString()),
                showLoading = false,
            )
        }
    }

    fun onRoastLevelChanged(roastLevel: String) {
        _viewState.update { viewState ->
            viewState.copy(
                bean = viewState.bean.copy(roastLevel = RoastLevel.valueOf(roastLevel)),
                showLoading = false,
            )
        }
    }

    fun onProcessChanged(process: String) {
        _viewState.update { viewState ->
            viewState.copy(
                bean = viewState.bean.copy(process = process.toProcessMethod()),
                showLoading = false,
            )
        }
    }

    fun onRoastLevelClick() {
        // τοδο
    }

    fun onProcessClick() {
        // τοδο
    }

    fun addBean(bean: Bean) {
        viewModelScope.launch {
            val result = addBeanUseCase(bean)

            when (result) {
                is Result.Success -> {
                    // Update the view state to success state.
                }
                is Result.Error -> {
                    // Update the view state to error state.
                }
                is Result.Loading -> {
                    // Update the view state to loading.
                }
            }
        }
    }
}
