package aeropresscipe.divinelink.aeropress.home.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeHomeRepository
import aeropresscipe.divinelink.aeropress.home.HomeState
import aeropresscipe.divinelink.aeropress.home.HomeViewModel
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakeThemedActivityDelegate
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

    fun assertViewState(
        expectedViewState: HomeState,
    ) = apply {
        val actualViewState = viewModel.statesList
        assertThat(actualViewState).contains(expectedViewState)
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

    suspend fun mockGetRecipe(
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
