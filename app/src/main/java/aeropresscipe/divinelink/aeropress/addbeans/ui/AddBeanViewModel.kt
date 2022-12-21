package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchBeanUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(
    private val fetchBeanUseCase: FetchBeanUseCase,
    private val addBeanUseCase: AddBeanUseCase,
) : ViewModel() {

    private val _viewState: MutableStateFlow<AddBeanViewState> =
        MutableStateFlow(AddBeanViewState.Initial)
    val viewState: StateFlow<AddBeanViewState> = _viewState

    fun getBean(bean: Bean) {
        viewModelScope.launch {
            fetchBeanUseCase(bean)
        }
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
