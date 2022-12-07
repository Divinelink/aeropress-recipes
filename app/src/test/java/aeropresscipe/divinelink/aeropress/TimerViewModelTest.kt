package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.fakes.FakeTimerRepository
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.recipe.models.remainingWater
import aeropresscipe.divinelink.aeropress.settings.app.notifications.usecase.GetTimerSoundUseCase
import aeropresscipe.divinelink.aeropress.test.util.fakes.FakePreferenceStorage
import aeropresscipe.divinelink.aeropress.timer.ITimerViewModel
import aeropresscipe.divinelink.aeropress.timer.TimerIntents
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
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
class TimerViewModelTest {
    private lateinit var viewModel: TimerViewModel
    private lateinit var viewModelIntent: TimerIntents

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatcher = mainDispatcherRule.testDispatcher

    private var transferableModel = TimerTransferableModel()

    private var repository: FakeTimerRepository = FakeTimerRepository()

    private fun initViewModel(
        getTimerSoundUseCase: GetTimerSoundUseCase = GetTimerSoundUseCase(FakePreferenceStorage(), testDispatcher),
    ) {
        viewModel = TimerViewModel(
            repository = repository.mock,
            getTimerSoundUseCase = getTimerSoundUseCase
        )

        viewModel.delegate = WeakReference(object :
            ITimerViewModel {
            override fun updateState(state: TimerState) {
                // do nothing
            }
        })

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
        val response = DiceDomain(
            recipeModel(bloomTime = 5, brewTime = 10), isBrewing = true, timeStartedMillis = System.currentTimeMillis()
        )
        repository.mockResume(response)
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
        repository.mockResume(response)
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
        repository.mockResume(response)
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
            recipe = recipeModel(bloomTime = 0, brewTime = 100),
            isBrewing = true,
            timeStartedMillis = System.currentTimeMillis()
        )
        repository.mockResume(response)
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
        initViewModel(GetTimerSoundUseCase(FakePreferenceStorage(timerSound = true), testDispatcher))
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 0), isBrewing = true)

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
        //        whenever(sharedPreferences.muteSound).thenReturn(true)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertTrue(viewModel.state is TimerState.UpdateProgressBar)
    }

    @Test
    fun `given mute sound preference false, when updateTimer, then I expect PlaySoundState`() = runTest {
        initViewModel(
            getTimerSoundUseCase = GetTimerSoundUseCase(
                FakePreferenceStorage(timerSound = true),
                testDispatcher
            )
        )
        val response = DiceDomain(recipeModel(brewTime = 1, bloomTime = 1), isBrewing = true)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        viewModel.updateTimer()
        assertTrue(viewModel.transferableModel?.brew?.getCurrentState() is BrewState.BrewWithBloom)
        assertTrue(viewModel.state is TimerState.PlaySoundState)
    }

    @Test
    fun `given recipe is Brewing, when I startBrew, then I expect resume`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 50, bloomTime = 20), isBrewing = true)
        repository.mockResume(response)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)
        viewModel.startBrew()
        viewModel.startBrew()

        assertTrue(viewModel.statesList[1] is TimerState.UpdateDescriptionState)
        assertTrue(viewModel.statesList[2] is TimerState.UpdateProgressBar)
    }

    @Test
    fun `given times are finished, when I resume, then I expect finish recipe brewing`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = -5, bloomTime = -5), isBrewing = true)
        repository.mockResume(response)
        repository.mockUpdateBrewingState(false, 0L, Unit)

        viewModel.resume()

        assertTrue(viewModel.state is TimerState.FinishState)
    }

    @Test
    fun `given a recipe, when I exit timer, then I expect ExitState`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 50, bloomTime = 20), isBrewing = true)
        transferableModel.recipe = response.recipe
        viewModel.init(transferableModel)

        viewModel.exitTimer()
        assertTrue(viewModel.state is TimerState.ExitState)
    }

    @Test
    fun `given a recipe with no time left, when I exit timer, then I expect brewing set to false`() = runTest {
        val isBrewing = true
        val response = DiceDomain(recipeModel(brewTime = 0, bloomTime = 0), isBrewing = isBrewing)
        transferableModel.recipe = response.recipe

        viewModel.init(transferableModel)

        repository.mockUpdateBrewingState(!isBrewing, 0L, Unit)
        viewModel.exitTimer()
    }

    @Test
    fun `given init is not called, when I start brew, then I expect error state`() = runTest {
        val response = DiceDomain(recipeModel(brewTime = 50, bloomTime = 20), isBrewing = false)
        transferableModel.recipe = response.recipe

        viewModel.startBrew()

        assertTrue(viewModel.statesList[0] is TimerState.ErrorState)
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
