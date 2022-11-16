package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchAllBeansUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BeansTrackerViewModel @Inject constructor(
    private val fetchAllBeansUseCase: FetchAllBeansUseCase
) : ViewModel() {

}
