package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.fakes.FakeRecipeRepository
import aeropresscipe.divinelink.aeropress.recipe.IRecipeViewModel
import aeropresscipe.divinelink.aeropress.recipe.RecipeIntents
import aeropresscipe.divinelink.aeropress.recipe.RecipeState
import aeropresscipe.divinelink.aeropress.recipe.RecipeViewModel
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.recipe.models.buildSteps
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
class RecipeViewModelTest {
    private lateinit var viewModel: RecipeViewModel
    private lateinit var viewModelIntent: RecipeIntents

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private var timerRemote: TimerServices = mock()

    private var repository = FakeRecipeRepository()
    private var timerRepository: TimerRepository = TimerRepository(timerRemote)

    private val sunday9am: LocalDateTime = LocalDateTime(2021, 7, 4, 9, 0, 0)
    private val sunday130pm: LocalDateTime = LocalDateTime(2021, 7, 4, 13, 30, 0)
    private val sunday930pm: LocalDateTime = LocalDateTime(2021, 7, 4, 21, 30, 0)

    private fun initViewModel() {
        viewModel = RecipeViewModel(
            delegate = WeakReference(object :
                IRecipeViewModel {
                override fun updateState(state: RecipeState) {
                    // do nothing
                }
            }),
            repository = repository.mock,
            timerRepository = timerRepository
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
    fun `given recipe is brewing, when I forceGenerateRecipe then I expect RefreshRecipeState`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(), isBrewing = true)
        repository.mockCreateNewRecipe(response)
        // When
        viewModel.forceGenerateRecipe()
        // Then
        assertTrue(viewModel.statesList[0] is RecipeState.RefreshRecipeState)
        assertEquals(viewModel.statesList[1], RecipeState.UpdateSavedIndicator(DISLIKE_MAX_FRAME))
        assertEquals(viewModel.statesList[2], RecipeState.HideResumeButtonState)
    }

    @Test
    fun `given recipe is not brewing, when I forceGenerateRecipe then I expect RefreshRecipeState`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(), isBrewing = false)
        repository.mockCreateNewRecipe(response)
        // When
        viewModel.forceGenerateRecipe()
        // Then
        assertTrue(viewModel.statesList[0] is RecipeState.RefreshRecipeState)
        assertEquals(viewModel.statesList[1], RecipeState.UpdateSavedIndicator(DISLIKE_MAX_FRAME))
        assertEquals(viewModel.statesList[2], RecipeState.HideResumeButtonState)
    }

    @Test
    fun `given time is 0900 am, then I expect morning message`() {
        viewModel.init(sunday9am.hour)

        assertTrue(viewModel.statesList[0] is RecipeState.InitialState)
        assertEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_morning))
        assertNotEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_afternoon))
        assertNotEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_evening))
    }

    @Test
    fun `given time is 130 PM, then I expect afternoon message`() {
        viewModel.init(sunday130pm.hour)

        assertTrue(viewModel.statesList[0] is RecipeState.InitialState)
        assertEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_afternoon))
        assertNotEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_morning))
        assertNotEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_evening))
    }

    @Test
    fun `given time is 2130 PM, then I expect evening message`() {
        viewModel.init(sunday930pm.hour)
        assertTrue(viewModel.statesList[0] is RecipeState.InitialState)
        assertEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_evening))
        assertNotEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_morning))
        assertNotEquals(viewModel.statesList[1], RecipeState.UpdateToolbarState(R.string.good_afternoon))
    }

    @Test
    fun `given dice is not null, when I getRecipe then I expect ShowRecipeState`() = runTest {
        val response = DiceDomain(recipeModel(), isBrewing = false)
        repository.mockGetRecipe(response)
        // todo wrap timer repository in a mock wrapper
        viewModel.getRecipe(false)
        assertTrue(viewModel.statesList[0] is RecipeState.ShowRecipeState)
    }

    @Test
    fun `given dice is not null, when I refresh then I expect ShowRecipeState`() = runTest {
        val response = DiceDomain(recipeModel(), isBrewing = false)
        repository.mockGetRecipe(response)
        viewModel.getRecipe(true)
        assertTrue(viewModel.statesList[0] is RecipeState.ShowRecipeState)
    }

    @Test
    fun `given recipe is brewing, when I generate Recipe, then I expect ShowAlreadyBrewingState`() = runTest {
        repository.mockGenerateRecipe(true)
        viewModel.generateRecipe()
        assertTrue(viewModel.statesList[0] is RecipeState.ShowAlreadyBrewingState)
    }

    @Test
    fun `given recipe is not brewing, when I generate Recipe, then I expect createNewRecipe`() = runTest {
        val response = DiceDomain(recipeModel(), isBrewing = false)

        repository.mockGenerateRecipe(false)
        repository.mockCreateNewRecipe(response)

        viewModel.generateRecipe()

        assertEquals(viewModel.statesList[0], RecipeState.RefreshRecipeState(response.recipe.buildSteps()))
        assertEquals(viewModel.statesList[1], RecipeState.UpdateSavedIndicator(DISLIKE_MAX_FRAME))
    }

    @Test
    fun `given a dice, when I start timer, then I expect StartTimerState with Start Flow`() = runTest {
        val response = DiceDomain(recipeModel())
        repository.mockCreateNewRecipe(response)
        viewModel.forceGenerateRecipe()

        viewModel.statesList.clear()

        viewModel.startTimer(false)
        assertEquals(viewModel.statesList[0], RecipeState.StartTimerState(recipeModel(), TimerFlow.START))
    }

    @Test
    fun `given a dice, when I resume, then I expect StartTimerState with Resume Flow`() = runTest {
        val response = DiceDomain(recipeModel())
        repository.mockCreateNewRecipe(response)
        viewModel.forceGenerateRecipe()

        viewModel.statesList.clear()

        viewModel.startTimer(true)
        assertEquals(viewModel.statesList[0], RecipeState.StartTimerState(recipeModel(), TimerFlow.RESUME))
    }

    @Test
    fun `given a dice that is brewing, when I resume, then I expect StartTimerState with Resume Flow`() = runTest {
        val response = DiceDomain(recipeModel(), isBrewing = false)
        repository.mockGenerateRecipe(false)
        repository.mockCreateNewRecipe(response)
        viewModel.generateRecipe()

        viewModel.statesList.clear()

        viewModel.startTimer(true)
        assertEquals(viewModel.statesList[0], RecipeState.StartTimerState(recipeModel(), TimerFlow.RESUME))
    }

    // Todo likeRecipe tests
    // Todo updateRecipeStates on timer repository

    private fun recipeModel(
        diceTemperature: Int = 0,
        brewTime: Long = 0,
        bloomTime: Long = 0,
        bloomWater: Int = 0,
        coffeeAmount: Int = 0,
        brewWaterAmount: Int = 0,
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
