package aeropresscipe.divinelink.aeropress.views.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.components.timerprogressview.TimerProgressIntents
import aeropresscipe.divinelink.aeropress.components.timerprogressview.TimerProgressState
import aeropresscipe.divinelink.aeropress.components.timerprogressview.TimerProgressViewModel
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class TimerProgressViewModelTest {
  private lateinit var viewModel: TimerProgressViewModel
  private lateinit var viewModelIntent: TimerProgressIntents

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private fun initViewModel() {
    viewModel = TimerProgressViewModel()
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
  fun `given a null dice with when I init then I expect Retry State`() {
    viewModel.init(null)
    assertTrue(viewModel.statesList[0] is TimerProgressState.RetryState)
  }

  @Test
  fun `given a correct dice with when I init then I expect Initial State`() {
    viewModel.init(DiceDomain(recipe()))
    assertTrue(viewModel.statesList[0] is TimerProgressState.InitialState)
  }

  @Test
  fun `given bloom time left is not zero, when I getView then I expect Start Timer State for bloom`() {
    viewModel.init(
      DiceDomain(
        recipe(bloomTime = 50),
        timeStartedMillis = System.currentTimeMillis(),
      ),
    )
    viewModel.statesList.clear()
    viewModel.getView()

    assertTrue(viewModel.statesList[0] is TimerProgressState.UpdateDescriptionState)
    assertTrue(viewModel.statesList[1] is TimerProgressState.UpdateProgressBar)
  }

  @Test
  fun `given brew time left is not zero, when I getView then I expect Start Timer State for bloom`() {
    viewModel.init(
      DiceDomain(
        recipe = recipe(bloomTime = 0, brewTime = 50),
        timeStartedMillis = System.currentTimeMillis(),
      ),
    )
    viewModel.statesList.clear()
    viewModel.getView()

    assertTrue(viewModel.statesList[0] is TimerProgressState.UpdateDescriptionState)
    assertTrue(viewModel.statesList[1] is TimerProgressState.UpdateProgressBar)
  }

  @Test
  fun `given brew is finish, when I getView then I expect Start Timer State for bloom`() {
    viewModel.init(DiceDomain(recipe(bloomTime = 50, brewTime = 50), timeStartedMillis = 10))
    viewModel.statesList.clear()
    viewModel.getView()

    assertThat(
      viewModel.statesList[0],
    ).isEqualTo(
      TimerProgressState.UpdateDescriptionState(BrewState.Finished),
    )
  }

  @Test
  fun `when update is called then i expect timer states`() {
    viewModel.init(DiceDomain(recipe(bloomTime = 50, brewTime = 50), timeStartedMillis = 50))
    viewModel.statesList.clear()
    viewModel.updateTimer(true)
    assertTrue(viewModel.statesList[0] is TimerProgressState.UpdateDescriptionState)
    assertTrue(viewModel.statesList[1] is TimerProgressState.UpdateProgressBar)
  }

  private fun recipe(
    diceTemperature: Int = 0,
    brewTime: Long = 0,
    bloomTime: Long = 0,
    bloomWater: Int = 10,
    coffeeAmount: Int = 0,
    brewWaterAmount: Int = 15,
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
