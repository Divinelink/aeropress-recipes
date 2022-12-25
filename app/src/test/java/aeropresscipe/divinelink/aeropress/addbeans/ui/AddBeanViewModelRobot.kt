package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.fakes.usecase.FakeAddBeanUseCase
import aeropresscipe.divinelink.aeropress.fakes.usecase.FakeUpdateBeanUseCase
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class AddBeanViewModelRobot {

    private lateinit var viewModel: AddBeanViewModel

    private val fakeAddBeanUseCase = FakeAddBeanUseCase()
    private val fakeUpdateBeanUseCase = FakeUpdateBeanUseCase()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    fun buildViewModel() = apply {
        viewModel = AddBeanViewModel(
            addBeanUseCase = fakeAddBeanUseCase.mock,
            updateBeanUseCase = fakeUpdateBeanUseCase.mock,
        )
    }

    suspend fun mockAddBeanResult(
        response: Result<AddBeanResult>,
    ) = apply {
        fakeAddBeanUseCase.mockAddBeanResult(response)
    }

    suspend fun mockUpdateBeanResult(
        response: Result<Unit>,
    ) = apply {
        fakeUpdateBeanUseCase.mockResultUpdateBean(response)
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

    fun onSetBean(bean: Bean?) = apply {
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

    fun onSubmitClicked() = apply {
        viewModel.onSubmitClicked()
    }
}
