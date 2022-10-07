package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.base.di.Preferences
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.recipe.models.remainingWater
import aeropresscipe.divinelink.aeropress.timer.ITimerViewModel
import aeropresscipe.divinelink.aeropress.timer.TimerIntents
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerServices
import aeropresscipe.divinelink.aeropress.timer.TimerState
import aeropresscipe.divinelink.aeropress.timer.TimerViewModel
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import android.content.SharedPreferences
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

    @Mock
    private var mockPrefs: SharedPreferences = mock()

    @Mock
    private var sharedPreferences: Preferences = Preferences(mockPrefs)

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
            repository = repository
        )

        viewModelIntent = viewModel
        viewModel.preferences = this.sharedPreferences
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
        val response = DiceDomain(
            recipeModel(bloomTime = 5, brewTime = 10), isBrewing = true, timeStartedMillis = System.currentTimeMillis()
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Bloom)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
        assertEquals(
            viewModel.statesList[1],
            TimerState.UpdateDescriptionState(
                BrewState.Bloom(
                    water = response.recipe.bloomWater,
                    time = response.recipe.bloomTime
                ),
                false
            )
        )
    }

    @Test
    fun `given bloom state, when I resume then I expect UpdateProgressBar state`() = runTest {
        // Given
        val response = DiceDomain(
            recipeModel(bloomTime = 5, brewTime = 10), isBrewing = true,
            timeStartedMillis = System.currentTimeMillis()
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        assertTrue(viewModel.statesList[0] is TimerState.InitialState)
        assertEquals(
            viewModel.statesList[1],
            TimerState.UpdateDescriptionState(
                BrewState.Bloom(
                    water = response.recipe.bloomWater,
                    time = response.recipe.bloomTime
                ),
                false
            )
        )
        assertTrue(viewModel.statesList[2] is TimerState.UpdateProgressBar)
    }

    @Test
    fun `given bloom state with 0 seconds left, when I resume then I expect Brew State`() = runTest {
        // Given
        val response = DiceDomain(
            recipeModel(bloomTime = 50, brewTime = 100),
            isBrewing = true,
            timeStartedMillis = System.currentTimeMillis() - 50000
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Bloom)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
    }

    @Test
    fun `given brew state, when I resume then I expect Brew State`() = runTest {
        // Given
        val response = DiceDomain(
            recipeModel(bloomTime = 0, brewTime = 100), isBrewing = true, timeStartedMillis = System.currentTimeMillis()
        )
        whenever(remote.getResumeTimes()).thenReturn(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        // When
        viewModel.resume()
        // Then
        val states = mutableListOf(
            BrewState.Brew(response.recipe.brewWaterAmount, response.recipe.brewTime),
            BrewState.Finished
        )
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
        assertEquals(viewModel.transferableModel?.brew?.brewStates, states)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Bloom)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
    }

    @Test
    fun `given bloom state, when update timer, then I expect brew state`() = runTest {
        val response = DiceDomain(
            recipeModel(brewTime = 1, bloomTime = 1, brewWaterAmount = 200, bloomWater = 50),
            isBrewing = true
        )

        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)
        viewModel.startBrew()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Bloom)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertEquals(
            viewModel.statesList[3],
            TimerState.UpdateDescriptionState(
                BrewState.BrewWithBloom(
                    water = response.recipe.remainingWater(),
                    time = response.recipe.brewTime, totalWater = response.recipe.brewWaterAmount
                ),
                true
            )
        )
    }

    @Test
    fun `given no bloomTime, when I start timer, then I expect brew state`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 0), isBrewing = true)

        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)
        viewModel.startBrew()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
        assertTrue(viewModel.statesList[0] is TimerState.InitialState)
        assertTrue(viewModel.statesList[1] is TimerState.UpdateDescriptionState)
        assertTrue(viewModel.statesList[2] is TimerState.UpdateProgressBar)
    }

    @Test
    fun `given brew state, when I update timer, then I expect finish state`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 0), isBrewing = true)
        whenever(sharedPreferences.muteSound).thenReturn(false)

        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Finished)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Brew)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertFalse(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.Bloom)
        assertTrue(viewModel.statesList[1] is TimerState.FinishState)
        assertTrue(viewModel.statesList[2] is TimerState.PlaySoundState)
    }

    @Test
    fun `given recipe is null, when I startBrew, then I expect ErrorState`() = runTest {
        transferableModel.recipe = null
        viewModel.startBrew()
        assert(viewModel.state is TimerState.ErrorState)
    }

    @Test
    fun `given mute sound preference true, when updateTimer, then I don't expect PlaySoundState`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 1), isBrewing = true)
        whenever(sharedPreferences.muteSound).thenReturn(true)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertTrue(viewModel.state is TimerState.UpdateProgressBar)
    }

    @Test
    fun `given mute sound preference false, when updateTimer, then I expect PlaySoundState`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 1), isBrewing = true)
        whenever(sharedPreferences.muteSound).thenReturn(false)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertTrue(viewModel.state is TimerState.PlaySoundState)
    }

    private fun recipeModel(
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
