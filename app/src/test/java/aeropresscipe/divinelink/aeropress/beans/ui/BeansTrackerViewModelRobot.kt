package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchAllBeansUseCase
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class BeansTrackerViewModelRobot {

    private lateinit var viewModel: BeansTrackerViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private val fakeBeanRepository = FakeBeanRepository()

    fun buildViewModel() = apply {
        viewModel = BeansTrackerViewModel(
            fetchAllBeansUseCase = FetchAllBeansUseCase(
                beanRepository = fakeBeanRepository.mock,
                dispatcher = testDispatcher
            )
        )
    }

    fun assertViewState(
        expectedViewState: BeanTrackerViewState,
    ) = apply {
        assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
    }

    fun assertFalseViewState(
        expectedViewState: BeanTrackerViewState,
    ) = apply {
        assertThat(viewModel.viewState.value).isNotEqualTo(expectedViewState)
    }

    fun mockFetchAllBeans(
        response: Result<List<Bean>>,
    ) = apply {
        fakeBeanRepository.mockFetchAllBeansResult(
            response
        )
    }

    fun onAddButtonClicked() = apply {
        viewModel.onAddButtonClicked()
    }

    fun onAddBeanOpened() = apply {
        viewModel.onAddBeanOpened()
    }

    fun onBeanClicked(bean: Bean) = apply {
        viewModel.onBeanClicked(bean)
    }
}
