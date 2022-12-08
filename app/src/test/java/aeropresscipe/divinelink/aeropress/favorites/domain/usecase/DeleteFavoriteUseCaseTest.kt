package aeropresscipe.divinelink.aeropress.favorites.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeFavoritesRepository
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import com.google.common.truth.Truth
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteFavoriteUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private var favoritesRepository = FakeFavoritesRepository()

    private val favorites = (1..12).map { index ->
        Favorites(
            recipe = Recipe(
                diceTemperature = index,
                brewTime = index.toLong(),
                bloomTime = index.toLong(),
                bloomWater = index,
                coffeeAmount = index,
                brewWaterAmount = index,
                grindSize = CoffeeGrindSize.values().getOrElse(index, defaultValue = { CoffeeGrindSize.COARSE }),
                brewMethod = BrewMethod.values().getOrElse(index, defaultValue = { BrewMethod.INVERTED })
            ),
            dateBrewed = ""
        )
    }.toMutableList()

    private val expectedList = (1..12).map { index ->
        Favorites(
            recipe = Recipe(
                diceTemperature = index,
                brewTime = index.toLong(),
                bloomTime = index.toLong(),
                bloomWater = index,
                coffeeAmount = index,
                brewWaterAmount = index,
                grindSize = CoffeeGrindSize.values().getOrElse(index, defaultValue = { CoffeeGrindSize.COARSE }),
                brewMethod = BrewMethod.values().getOrElse(index, defaultValue = { BrewMethod.INVERTED })
            ),
            dateBrewed = ""
        )
    }.toMutableList()

    @Test
    fun `given a list of favorites, when I remove some items, then I expect a list without those items`() = runTest {
        val response = Result.Success(
            favorites
                .withoutItemAt(0)
                .withoutItemAt(5)
                .withoutItemAt(4)
        )

        favoritesRepository.mockDeleteFavorite(
            recipe = favorites[0].recipe,
            response = response
        )

        val useCase = DeleteFavoriteUseCase(
            repository = favoritesRepository.mock,
            dispatcher = testDispatcher,
        )

        val result = useCase(favorites[0].recipe)

        Truth.assertThat(
            result.first()
        ).isEqualTo(
            Result.Success(
                expectedList
                    .withoutItemAt(0)
                    .withoutItemAt(5)
                    .withoutItemAt(4)
            )
        )
    }

    private fun <T> Iterable<T>.withoutItemAt(index: Int): List<T> =
        filterIndexed { i, _ -> i != index }

    @Test
    fun `unknown failure on fetching favorites`() = runTest {
        val failureResponse = Result.Error(
            Exception("You messed up")
        )

        favoritesRepository.mockDeleteFavorite(
            recipe = favorites[0].recipe,
            response = failureResponse
        )

        val useCase = DeleteFavoriteUseCase(
            repository = favoritesRepository.mock,
            dispatcher = testDispatcher
        )
        val result = useCase(favorites[0].recipe)

        Truth.assertThat(
            result.first().toString()
        ).isEqualTo(
            Result.Error(Exception("You messed up")).toString()
        )

        Truth.assertThat(
            result.first().toString()
        ).isNotEqualTo(
            Result.Error(Exception()).toString()
        )
    }

    @Test
    fun `successful loading state`() = runTest {
        val response = Result.Loading

        favoritesRepository.mockDeleteFavorite(
            recipe = favorites[0].recipe,
            response = response
        )

        val useCase = DeleteFavoriteUseCase(
            repository = favoritesRepository.mock,
            dispatcher = testDispatcher
        )
        val result = useCase(favorites[0].recipe)

        Truth.assertThat(result.first()).isEqualTo(Result.Loading)
    }
}
