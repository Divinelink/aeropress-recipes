package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class AddBeanViewModelRobot {

    private lateinit var viewModel: AddBeanViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private val fakeBeanRepository = FakeBeanRepository()

    fun buildViewModel() = apply {
        viewModel = AddBeanViewModel(
            addBeanUseCase = AddBeanUseCase(
                beanRepository = fakeBeanRepository.mock,
                dispatcher = testDispatcher
            )
        )
    }

    fun assertViewState(
        expectedViewState: AddBeanViewState,
    ) = apply {
        assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
    }

    fun assertFalseViewState(
        expectedViewState: AddBeanViewState,
    ) = apply {
        assertThat(viewModel.viewState.value).isNotEqualTo(expectedViewState)
    }

    fun onSetBean(bean: Bean) = apply {
        viewModel.setBean(bean)
    }

    fun onBeanNameChanged(name: String) = apply {
        viewModel.onBeanNameChanged(name)
    }

    fun onRoasterNameChanged(name: String) = apply {
        viewModel.onRoasterNameChanged(name)
    }

    fun onOriginChanged(origin: String) = apply {
        viewModel.onOriginChanged(origin)
    }

    fun onDateChanged(date: LocalDate) = apply {
        viewModel.onDateChanged(date)
    }

    fun onRoastLevelChanged(roastLevel: String) = apply {
        viewModel.onRoastLevelChanged(roastLevel)
    }

    fun onProcessChanged(process: String) = apply {
        viewModel.onProcessChanged(process)
    }

    fun onRoastLevelClicked() = apply {
        viewModel.onRoastLevelClicked()
    }

    fun onProcessClicked() = apply {
        viewModel.onProcessClicked()
    }

    fun onAddBean(bean: Bean) = apply {
        viewModel.addBean(bean)
    }
}
