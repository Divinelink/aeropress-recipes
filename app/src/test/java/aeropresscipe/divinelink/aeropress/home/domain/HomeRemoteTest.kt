package aeropresscipe.divinelink.aeropress.home.domain

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.dao.FakeRecipeDao
import aeropresscipe.divinelink.aeropress.home.HomeRemote
import aeropresscipe.divinelink.aeropress.home.HomeRepository
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeRemoteTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private var recipeDao = FakeRecipeDao()
  private val homeRemote = HomeRemote(
    dispatcher = testDispatcher,
    recipeDao = recipeDao.mock,
  )

  private lateinit var repository: HomeRepository

  private val testDice = DiceDomain(
    recipe = Recipe(
      diceTemperature = 0,
      brewTime = 0,
      bloomTime = 0,
      bloomWater = 0,
      coffeeAmount = 0,
      brewWaterAmount = 0,
      grindSize = CoffeeGrindSize.COARSE,
      brewMethod = BrewMethod.STANDARD,
    ),
    isBrewing = true,
    timeStartedMillis = 0,
  )

  @Before
  fun setUp() {
    repository = HomeRepository(homeRemote)
  }

  @Test
  fun testGetRecipe() = runTest {
    recipeDao.mockGetRecipe(testDice)

    val result = homeRemote.getRecipe()

    assertThat(result).isNotNull()
    recipeDao.verifyGetRecipe()
  }

  @Test
  fun testUpdateRecipe_UpdateTrue() = runTest {
    // Arrange
    recipeDao.mockUpdateRecipe(testDice.recipe)
    recipeDao.mockGetRecipe(testDice)
    // Act
    val result = homeRemote.updateRecipe(testDice.recipe, true)

    // Assert
    assertThat(result).isNotNull()
    recipeDao.verifyUpdateRecipe(testDice.recipe)
    recipeDao.verifyGetRecipe()
  }

  @Test
  fun testUpdateRecipe_UpdateFalse() = runTest {
    // Arrange
    recipeDao.mockGetRecipe(testDice)
    // Act
    val result = homeRemote.updateRecipe(testDice.recipe, false)

    // Assert
    assertThat(result).isNotNull()
    recipeDao.verifyGetRecipe()
  }
}
