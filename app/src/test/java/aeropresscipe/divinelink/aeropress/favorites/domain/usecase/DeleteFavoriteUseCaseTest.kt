package aeropresscipe.divinelink.aeropress.favorites.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeFavoritesRepository
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteFavoriteUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private var favoritesRepository = FakeFavoritesRepository()

  @Test
  fun deleteFavoriteRecipe() = runTest {
    val favoriteRecipe = Favorites(
      recipe = Recipe(
        diceTemperature = 50,
        brewTime = 30,
        bloomTime = 5,
        bloomWater = 100,
        coffeeAmount = 20,
        brewWaterAmount = 10,
        grindSize = CoffeeGrindSize.COARSE,
        brewMethod = BrewMethod.INVERTED,
      ),
      dateBrewed = "",
    )

    favoritesRepository.deleteFavoritesResult[favoriteRecipe.recipe] = Result.Success(Unit)

    val useCase = DeleteFavoriteUseCase(
      repository = favoritesRepository,
      dispatcher = testDispatcher,
    )
    useCase.invoke(favoriteRecipe.recipe)
  }
}
