package aeropresscipe.divinelink.aeropress.favorites.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeFavoritesRepository
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.FavoritesViewModel
import aeropresscipe.divinelink.aeropress.favorites.domain.usecase.DeleteFavoriteUseCase
import aeropresscipe.divinelink.aeropress.favorites.domain.usecase.FetchAllFavoritesUseCase
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelRobot {

    private lateinit var viewModel: FavoritesViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private val fakeFavoritesRepository = FakeFavoritesRepository()

    fun buildViewModel() = apply {
        viewModel = FavoritesViewModel(
            deleteFavoriteUseCase = DeleteFavoriteUseCase(fakeFavoritesRepository.mock, testDispatcher),
            fetchAllFavoritesUseCase = FetchAllFavoritesUseCase(fakeFavoritesRepository.mock, testDispatcher)
        )
    }

    fun assertViewState(expectedViewState: FavoritesViewState) = apply {
        val actualViewState = viewModel.viewState.value
        assertThat(actualViewState).isEqualTo(expectedViewState)
    }

    fun assertNotEqualViewState(expectedViewState: FavoritesViewState) = apply {
        val actualViewState = viewModel.viewState.value
        assertThat(actualViewState).isNotEqualTo(expectedViewState)
    }

    fun mockFetchAllFavorites(
        response: Result<List<Favorites>>,
    ) = apply {
        fakeFavoritesRepository.mockFetchAllFavoritesResult(response)
    }

    fun mockDeleteFavoriteRecipe(
        recipe: Recipe,
        response: Result<List<Favorites>>,
    ) = apply {
        fakeFavoritesRepository.mockDeleteFavorite(
            recipe,
            response
        )
    }

    fun onRefreshClick() = apply {
        viewModel.refresh()
    }

    fun onStartBrewClick(recipe: Recipe) = apply {
        viewModel.startBrewClicked(recipe)
    }

    fun deleteRecipe(recipe: Recipe) = apply {
        viewModel.deleteRecipe(recipe)
    }
}
