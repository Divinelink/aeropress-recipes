package aeropresscipe.divinelink.aeropress.favorites.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeFavoritesRepository
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FetchAllFavoritesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private var favoritesRepository = FakeFavoritesRepository()

  private val favorites = (1..12).map { index ->
    Favorites(
      recipe = Recipe(
        diceTemperature = 90,
        brewTime = 15,
        bloomTime = 10,
        bloomWater = index,
        coffeeAmount = index,
        brewWaterAmount = index,
        grindSize = CoffeeGrindSize.values()
          .getOrElse(index, defaultValue = { CoffeeGrindSize.COARSE }),
        brewMethod = BrewMethod.values().getOrElse(index, defaultValue = { BrewMethod.INVERTED }),
      ),
      dateBrewed = "",
    )
  }.toMutableList()

  @Test
  fun `successfully fetch all favorites`() = runTest {
    val response = Result.Success(favorites)

    favoritesRepository.mockFetchAllFavoritesResult(
      response = response,
    )

    val useCase = FetchAllFavoritesUseCase(
      repository = favoritesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(Unit)

    assertThat(
      result.first(),
    ).isEqualTo(
      Result.Success(favorites),
    )
  }

  @Test
  fun `unknown failure on fetching favorites`() = runTest {
    val failureResponse = Result.Error(
      Exception("You messed up"),
    )

    favoritesRepository.mockFetchAllFavoritesResult(
      response = failureResponse,
    )

    val useCase = FetchAllFavoritesUseCase(
      repository = favoritesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(Unit)

    assertThat(
      result.first().toString(),
    ).isEqualTo(
      Result.Error(Exception("You messed up")).toString(),
    )

    assertThat(
      result.first().toString(),
    ).isNotEqualTo(
      Result.Error(Exception()).toString(),
    )
  }

  @Test
  fun `successful loading state`() = runTest {
    val response = Result.Loading

    favoritesRepository.mockFetchAllFavoritesResult(
      response = response,
    )

    val useCase = FetchAllFavoritesUseCase(
      repository = favoritesRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(Unit)

    assertThat(result.first()).isEqualTo(Result.Loading)
  }
}
