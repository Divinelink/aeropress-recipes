package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.components.saverecipecard.ISaveRecipeCardViewModel
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardIntents
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardState
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.LIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardViewModel
import aeropresscipe.divinelink.aeropress.fakes.FakeTimerRepository
import aeropresscipe.divinelink.aeropress.history.LikeSnackBar
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.ref.WeakReference
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SaveRecipeCardViewTest {
  private lateinit var viewModel: SaveRecipeCardViewModel
  private lateinit var viewModelIntent: SaveRecipeCardIntents

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private var repository = FakeTimerRepository()

  private fun initViewModel() {
    viewModel = SaveRecipeCardViewModel(
      delegate = WeakReference(
        object :
          ISaveRecipeCardViewModel {
          override fun updateState(state: SaveRecipeCardState) {
            // do nothing
          }
        },
      ),
      repository = repository.mock,
    )

    viewModelIntent = viewModel
  }

  @Before
  fun before() {
    initViewModel()
  }

  @After
  fun after() {
    viewModel.statesList.forEach { println(it) }
  }

  @Test
  fun `given recipe is liked, on init return saved state`() = runTest {
    val recipe = recipeModel()
    repository.mockIsRecipeSaved(recipe, response = true)

    viewModel.init(recipeModel())

    assertEquals(viewModel.state, SaveRecipeCardState.UpdateSavedIndicator(LIKE_MAX_FRAME))
  }

  @Test
  fun `given recipe is not liked, on init return saved state`() = runTest {
    val recipe = recipeModel()
    repository.mockIsRecipeSaved(recipe, response = false)

    viewModel.init(recipe)

    assertEquals(viewModel.state, SaveRecipeCardState.UpdateSavedIndicator(DISLIKE_MAX_FRAME))
  }

  @Test
  fun `given recipe is not liked, when I like then I expect RecipeSavedState`() = runTest {
    val recipe = recipeModel()
    val expectedResponse = false
    repository.mockIsRecipeSaved(recipe, response = expectedResponse)
    repository.mockLikeRecipe(recipe, !expectedResponse)

    viewModel.likeRecipe(recipe)

    assertEquals(viewModel.statesList[0], SaveRecipeCardState.RecipeSavedState)
    assertEquals(viewModel.statesList[1], SaveRecipeCardState.ShowSnackBar(LikeSnackBar.Like))
  }

  @Test
  fun `given recipe is liked, when I like then I expect RecipeSavedState`() = runTest {
    val recipe = recipeModel()
    val expectedResponse = true
    repository.mockIsRecipeSaved(recipe, response = expectedResponse)
    repository.mockLikeRecipe(recipe, !expectedResponse)

    viewModel.likeRecipe(recipe)

    assertEquals(viewModel.statesList[0], SaveRecipeCardState.RecipeRemovedState)
    assertEquals(viewModel.statesList[1], SaveRecipeCardState.ShowSnackBar(LikeSnackBar.Remove))
  }

  private fun recipeModel(
    diceTemperature: Int = 0,
    brewTime: Long = 0,
    bloomTime: Long = 0,
    bloomWater: Int = 0,
    coffeeAmount: Int = 0,
    brewWaterAmount: Int = 0,
    groundSize: CoffeeGrindSize = CoffeeGrindSize.MEDIUM,
    brewingMethod: BrewMethod = BrewMethod.STANDARD,
  ): Recipe = Recipe(
    diceTemperature = diceTemperature,
    brewTime = brewTime,
    bloomTime = bloomTime,
    bloomWater = bloomWater,
    coffeeAmount = coffeeAmount,
    brewWaterAmount = brewWaterAmount,
    grindSize = groundSize,
    brewMethod = brewingMethod,
  )
}
