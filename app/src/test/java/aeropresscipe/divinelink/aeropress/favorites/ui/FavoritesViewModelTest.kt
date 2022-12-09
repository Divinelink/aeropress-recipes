package aeropresscipe.divinelink.aeropress.favorites.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {
    private val testRobot = FavoritesViewModelRobot()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val favorite = Favorites(
        recipe = Recipe(
            diceTemperature = 0,
            brewTime = 0,
            bloomTime = 0,
            bloomWater = 0,
            coffeeAmount = 0,
            brewWaterAmount = 0,
            grindSize = CoffeeGrindSize.COARSE,
            brewMethod = BrewMethod.INVERTED
        ),
        dateBrewed = ""
    )

    private val favoritesList = listOf(favorite)

    @Test
    fun successfulInitialState() = runTest {
        testRobot
            .mockFetchAllFavorites(
                Result.Loading
            )
            .buildViewModel()
            .assertViewState(FavoritesViewState())
            .assertNotEqualViewState(FavoritesViewState(isLoading = false))
    }

    @Test
    fun `successfully fetch all favorites`() = runTest {
        testRobot
            .mockFetchAllFavorites(Result.Success(favoritesList))
            .buildViewModel()
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    recipes = listOf(favorite),
                    errorMessage = null,
                    emptyRecipes = false
                )
            )
    }

    @Test
    fun `failed fetching all favorites resulting in error state`() = runTest {
        testRobot
            .mockFetchAllFavorites(Result.Error(Exception()))
            .buildViewModel()
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    errorMessage = Exception().toString()
                )
            )
    }

    @Test
    fun `given a list of recipes, when I click refresh then I expect success state`() = runTest {
        testRobot
            .mockFetchAllFavorites(
                Result.Success(favoritesList)
            )
            .buildViewModel()
            .onRefreshClick()
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    recipes = listOf(favorite),
                    emptyRecipes = false
                )
            )
    }

    @Test
    fun `given empty recipe list, on init, then I expect empty recipes to be true`() = runTest {
        testRobot
            .mockFetchAllFavorites(
                Result.Success(emptyList())
            )
            .buildViewModel()
            .onRefreshClick()
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    emptyRecipes = true
                )
            )
    }

    @Test
    fun `on initial error, when I refresh then I expect success state`() = runTest {
        testRobot
            .mockFetchAllFavorites(
                Result.Error(Exception("Initial error!"))
            )
            .buildViewModel()
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    recipes = null,
                    errorMessage = Exception("Initial error!").toString()
                )
            )
            .mockFetchAllFavorites(
                Result.Success(favoritesList)
            )
            .onRefreshClick()
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    emptyRecipes = false,
                    recipes = listOf(favorite),
                    errorMessage = null
                )
            )
    }

    @Test
    fun `successfully start new brew`() = runTest {
        testRobot
            .buildViewModel()
            .onStartBrewClick(favorite.recipe)
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    brewRecipe = favorite.recipe
                )
            )
    }

    @Test
    fun `given a list with one item, when I delete recipe, then i expect emptyList`() = runTest {
        testRobot
            .mockDeleteFavoriteRecipe(
                recipe = favorite.recipe,
                response = Result.Success(emptyList())
            )
            .buildViewModel()
            .deleteRecipe(favorite.recipe)
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    emptyRecipes = true
                )
            )
    }

    @Test
    fun `given a list of recipes, when I delete recipe, then i expect a list without that recipe`() = runTest {
        testRobot
            .mockDeleteFavoriteRecipe(
                recipe = favorite.recipe,
                response = Result.Success(favoritesList)
            )
            .buildViewModel()
            .deleteRecipe(favorite.recipe)
            .assertViewState(
                FavoritesViewState(
                    isLoading = false,
                    emptyRecipes = false,
                    recipes = favoritesList
                )
            )
    }
}
