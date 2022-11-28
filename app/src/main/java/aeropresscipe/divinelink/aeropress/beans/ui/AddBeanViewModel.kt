package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchBeanUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBeanViewModel @Inject constructor(
    private val fetchBeanUseCase: FetchBeanUseCase,
) : ViewModel() {

    fun getBean(bean: Bean) {
        viewModelScope.launch {
            fetchBeanUseCase(bean)
        }
    }
}
