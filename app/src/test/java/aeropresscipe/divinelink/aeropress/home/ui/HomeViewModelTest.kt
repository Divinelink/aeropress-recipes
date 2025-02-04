package aeropresscipe.divinelink.aeropress.home.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.home.HomeState
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

  private val testRobot = HomeViewModelTestRobot()

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

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun successfulInitState() = runTest {
    testRobot
      .buildViewModel()
      .onInit()
      .assertViewState(
        HomeState.InitialState,
      )
  }

  @Test
  fun `given recipe is brewing, when I resume then I expect ShowResumeButtonState`() = runTest {
    val dice = testDice.copy(isBrewing = true)
    testRobot
      .buildViewModel()
      .mockGetRecipe(response = dice)
      .onResume()
      .assertViewState(
        HomeState.ShowResumeButtonState(dice),
      )
  }

  @Test
  fun `given recipe is not brewing, when I resume then I expect ShowResumeButtonState`() = runTest {
    val dice = testDice.copy(isBrewing = false)
    testRobot
      .buildViewModel()
      .mockGetRecipe(response = dice)
      .onResume()
      .assertViewState(
        HomeState.HideResumeButtonState,
      )
  }

  @Test
  fun `when generateRecipe I expect HideResumeButtonState`() = runTest {
    testRobot
      .buildViewModel()
      .onGenerateRecipe()
      .assertViewState(HomeState.HideResumeButtonState)
  }

  @Test
  fun `given recipe is updated when I start timer then I expect StartTimerState`() = runTest {
    testRobot
      .buildViewModel()
      .mockUpdateRecipe(
        recipe = testDice.recipe,
        update = true,
        response = testDice,
      )
      .onStartTimer(
        recipe = testDice.recipe,
        flow = TimerFlow.START,
        update = true,
      )
      .assertViewState(
        HomeState.StartTimerState(testDice.recipe, TimerFlow.START),
      )
  }

  @Test
  fun `if recipe canResume when I resumeTimer then I expect StartTimerState with Resume flow`() =
    runTest {
      val dice = testDice.copy(isBrewing = true)
      testRobot
        .buildViewModel()
        .mockGetRecipe(response = dice)
        .onResume()
        .onResumeTimer()
        .assertViewState(
          HomeState.StartTimerState(
            recipe = testDice.recipe,
            flow = TimerFlow.RESUME,
          ),
        )
    }
}
