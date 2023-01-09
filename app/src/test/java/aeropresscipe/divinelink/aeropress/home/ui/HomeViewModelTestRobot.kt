package aeropresscipe.divinelink.aeropress.home.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeHomeRepository
import aeropresscipe.divinelink.aeropress.home.HomeState
import aeropresscipe.divinelink.aeropress.home.HomeViewModel
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakeThemedActivityDelegate
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTestRobot {

    private lateinit var viewModel: HomeViewModel
    private var repository: FakeHomeRepository = FakeHomeRepository()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    fun buildViewModel() = apply {
        viewModel = HomeViewModel(
            repository = repository.mock,
            themedActivityDelegate = FakeThemedActivityDelegate(),
        )
    }

    fun onInit() = apply {
        viewModel.init()
    }

    fun onResume() = apply {
        viewModel.resume()
    }

    fun onResumeTimer() = apply {
        viewModel.resumeTimer()
    }

    fun onGenerateRecipe() = apply {
        viewModel.generateRecipe()
    }

    fun onStartTimer(
        recipe: Recipe,
        flow: TimerFlow,
        update: Boolean,
    ) = apply {
        viewModel.startTimer(
            recipe = recipe,
            flow = flow,
            update = update,
        )
    }

    fun assertViewState(
        expectedViewState: HomeState,
    ) = apply {
        val actualViewStates = viewModel.statesList
        assertThat(actualViewStates).contains(expectedViewState)
    }

    fun assertFalseViewState(
        expectedViewState: HomeState,
    ) = apply {
        val actualViewState = viewModel.statesList
        assertThat(actualViewState).doesNotContain(expectedViewState)
    }

    suspend fun mockGetRecipe(
        response: DiceDomain,
    ) = apply {
        repository.mockGetRecipe(response)
    }

    suspend fun mockUpdateRecipe(
        recipe: Recipe,
        update: Boolean,
        response: DiceDomain,
    ) = apply {
        repository.mockUpdateRecipe(
            recipe = recipe,
            update = update,
            response = response,
        )
    }
}
