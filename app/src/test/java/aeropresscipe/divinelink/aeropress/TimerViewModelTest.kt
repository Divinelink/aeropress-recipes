package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.generaterecipe.BrewMethod
import aeropresscipe.divinelink.aeropress.generaterecipe.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.ITimerViewModel
import aeropresscipe.divinelink.aeropress.timer.TimerFragment
import aeropresscipe.divinelink.aeropress.timer.TimerIntents
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerServices
import aeropresscipe.divinelink.aeropress.timer.TimerState
import aeropresscipe.divinelink.aeropress.timer.TimerViewModel
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
class TimerViewModelTest {
    private lateinit var viewModel: TimerViewModel
    private lateinit var viewModelIntent: TimerIntents

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private var remote: TimerServices = mock()

    private var transferableModel = TimerTransferableModel()

    private var repository: TimerRepository = TimerRepository(remote)

    private fun initViewModel() {
        viewModel = TimerViewModel(
            delegate = WeakReference(object :
                ITimerViewModel {
                override fun updateState(state: TimerState) {
                    // do nothing
                }
            }),
            repository = repository)

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
    fun `given bloom state with 5 seconds left, when I resume then I expect Bloom State`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(bloomTime = 5, brewTime = 10), isBrewing = true,
            bloomEndTimeMillis = 5000 + System.currentTimeMillis(),
            brewEndTimeMillis = 10000 + System.currentTimeMillis()
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        assertTrue(viewModel.transferableModel?.currentBrewState is BrewState.Bloom)
        assertFalse(viewModel.transferableModel?.currentBrewState is BrewState.BrewWithBloom)
        assertFalse(viewModel.transferableModel?.currentBrewState is BrewState.Brew)
    }

    @Test
    fun `given bloom state with 0 seconds left, when I resume then I expect Brew State`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(bloomTime = 5, brewTime = 100), isBrewing = true,
            bloomEndTimeMillis = 0,
            brewEndTimeMillis = 10000 + System.currentTimeMillis()
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        assertTrue(viewModel.transferableModel?.currentBrewState is BrewState.BrewWithBloom)
        assertFalse(viewModel.transferableModel?.currentBrewState is BrewState.Bloom)
        assertFalse(viewModel.transferableModel?.currentBrewState is BrewState.Brew)
    }

    @Test
    fun `given brew state, when I resume then I expect Brew State`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(bloomTime = 0, brewTime = 100), isBrewing = true,
            bloomEndTimeMillis = 0,
            brewEndTimeMillis = 10000 + System.currentTimeMillis()
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        assertTrue(viewModel.transferableModel?.currentBrewState is BrewState.Brew)
        assertFalse(viewModel.transferableModel?.currentBrewState is BrewState.Bloom)
        assertFalse(viewModel.transferableModel?.currentBrewState is BrewState.BrewWithBloom)
    }

    @Test
    fun `given recipe is liked, on init return saved state`() = runTest {
        val response = DiceDomain(recipeModel())

        whenever(remote.isRecipeSaved(response.recipe)).thenReturn(true)
        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)

        assertEquals(viewModel.state, TimerState.UpdateSavedIndicator(TimerFragment.LIKE_MAX_FRAME))
    }

    @Test
    fun `given recipe is not liked, on init return saved state`() = runTest {
        val response = DiceDomain(recipeModel())

        whenever(remote.isRecipeSaved(response.recipe)).thenReturn(false)
        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)

        assertEquals(viewModel.state, TimerState.UpdateSavedIndicator(TimerFragment.DISLIKE_MAX_FRAME))
    }

    @Test
    fun `given bloom state, when update timer, then I expect brew state`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 1), isBrewing = true)

        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)
        viewModel.startBrew()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Bloom)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
    }

    @Test
    fun `given no bloomTime, when I start timer, then I expect brew state`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 0), isBrewing = true)

        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)
        viewModel.startBrew()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
        assertTrue(viewModel.statesList[0] is TimerState.InitialState)
        assertTrue(viewModel.statesList[1] is TimerState.StartTimer)
        assertTrue(viewModel.statesList[2] is TimerState.StartProgressBar)
    }

    @Test
    fun `given brew state, when I update timer, then I expect finish state`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 0), isBrewing = true)

        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Finished)
        assertTrue(viewModel.state is TimerState.FinishState)
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
        isNewRecipe: Boolean = false,
    ): Recipe {
        return Recipe(
            diceTemperature = diceTemperature,
            brewTime = brewTime,
            bloomTime = bloomTime,
            bloomWater = bloomWater,
            coffeeAmount = coffeeAmount,
            brewWaterAmount = brewWaterAmount,
            grindSize = groundSize,
            brewMethod = brewingMethod,
            isNewRecipe = isNewRecipe)
    }
}
