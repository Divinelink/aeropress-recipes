package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.fakes.FakeTimerRepository
import aeropresscipe.divinelink.aeropress.finish.FinishIntents
import aeropresscipe.divinelink.aeropress.finish.FinishState
import aeropresscipe.divinelink.aeropress.finish.FinishViewModel
import aeropresscipe.divinelink.aeropress.finish.IFinishViewModel
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.ref.WeakReference
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class FinishViewModelTest {
    private lateinit var viewModel: FinishViewModel
    private lateinit var viewModelIntent: FinishIntents

    private var repository = FakeTimerRepository()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun initViewModel() {
        viewModel = FinishViewModel(
            delegate = WeakReference(object :
                IFinishViewModel {
                override fun updateState(state: FinishState) {
                    // do nothing
                }
            }),
            repository = repository.mock
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
    fun `Initial State when View Model is created`() {
        viewModel.init(recipe())
        assertTrue(viewModel.statesList[0] is FinishState.InitialState)
        assertTrue(viewModel.statesList[1] is FinishState.SetupRecipeState)
        assertEquals(viewModel.statesList[1], FinishState.SetupRecipeState(recipe()))
    }

    @Test
    fun `When close button clicked then I expect Close State`() {
        viewModel.closeButtonClicked()
        assertTrue(viewModel.statesList[0] is FinishState.CloseState)
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
    ): Recipe {
        return Recipe(
            diceTemperature = diceTemperature,
            brewTime = brewTime,
            bloomTime = bloomTime,
            bloomWater = bloomWater,
            coffeeAmount = coffeeAmount,
            brewWaterAmount = brewWaterAmount,
            grindSize = groundSize,
            brewMethod = brewingMethod
        )
    }
}
