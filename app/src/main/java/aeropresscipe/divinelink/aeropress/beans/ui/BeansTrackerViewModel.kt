package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchAllBeansUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BeansTrackerViewModel @Inject constructor(
    private val fetchAllBeansUseCase: FetchAllBeansUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<BeanTrackerViewState> =
        MutableStateFlow(BeanTrackerViewState.Initial)
    val viewState: StateFlow<BeanTrackerViewState> = _viewState.asStateFlow()

    init {
        fetchAllBeansUseCase
    }
}
