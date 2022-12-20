package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.ViewModelTestRobot
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.FetchAllBeansUseCase
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class BeansTrackerViewModelRobot : ViewModelTestRobot<BeanTrackerViewState>() {

    private lateinit var viewModel: BeansTrackerViewModel

    override lateinit var actualViewState: BeanTrackerViewState

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private val fakeBeanRepository = FakeBeanRepository()

    override fun buildViewModel() = apply {
        viewModel = BeansTrackerViewModel(
            fetchAllBeansUseCase = FetchAllBeansUseCase(
                beanRepository = fakeBeanRepository.mock,
                dispatcher = testDispatcher
            )
        )
        actualViewState = viewModel.viewState.value
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

    fun onBeanClicked(bean: Bean) = apply {
        viewModel.onBeanClicked(bean)
    }
}
