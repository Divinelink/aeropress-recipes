package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchAllBeansUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeansTrackerViewModel @Inject constructor(
  private val fetchAllBeansUseCase: FetchAllBeansUseCase,
) : ViewModel() {

  private val _viewState: MutableStateFlow<BeanTrackerViewState> =
    MutableStateFlow(BeanTrackerViewState.Initial)
  val viewState: StateFlow<BeanTrackerViewState> = _viewState.asStateFlow()

  init {
    viewModelScope.launch {
      fetchAllBeansUseCase.invoke(Unit).collect { result ->
        if (result is Result.Success) {
          _viewState.value = BeanTrackerViewState.Completed(result.data)
        }
      }
    }
  }

  fun onAddButtonClicked() {
    _viewState.update { state ->
      (state as BeanTrackerViewState.Completed).copy(goToAddBean = true)
    }
  }

  fun onAddBeanOpened() {
    _viewState.update { state ->
      (state as BeanTrackerViewState.Completed).copy(
        selectedBean = null,
        goToAddBean = false,
      )
    }
  }

  fun onBackButtonClicked() {
    _viewState.value
  }

  /**
   * Navigates the user to the Add Bean screen but with all the [bean] properties prefilled.
   */
  fun onBeanClicked(bean: Bean) {
    _viewState.update { state ->
      (state as BeanTrackerViewState.Completed).copy(
        selectedBean = bean,
        goToAddBean = true,
      )
    }
  }
}
