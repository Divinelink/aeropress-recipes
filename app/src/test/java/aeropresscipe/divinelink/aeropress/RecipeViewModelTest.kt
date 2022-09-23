package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.recipe.GenerateRecipeRemote
import aeropresscipe.divinelink.aeropress.recipe.IRecipeViewModel
import aeropresscipe.divinelink.aeropress.recipe.RecipeIntents
import aeropresscipe.divinelink.aeropress.recipe.RecipeRepository
import aeropresscipe.divinelink.aeropress.recipe.RecipeState
import aeropresscipe.divinelink.aeropress.recipe.RecipeViewModel
import aeropresscipe.divinelink.aeropress.recipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.recipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
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
import org.mockito.kotlin.whenever
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
class RecipeViewModelTest {
    private lateinit var viewModel: RecipeViewModel
    private lateinit var viewModelIntent: RecipeIntents

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private var remote: GenerateRecipeRemote = mock()
    @Mock
    private var timerRemote: TimerServices = mock()

    private var repository: RecipeRepository = RecipeRepository(remote)
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
            repository = repository,
            timerRepository = timerRepository)

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

        whenever(remote.createNewRecipe()).thenReturn(response)
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
        whenever(remote.createNewRecipe()).thenReturn(response)
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

    private fun recipeModel(
        diceTemperature: Int = 0,
        brewTime: Long = 0,
        bloomTime: Long = 0,
        bloomWater: Int = 0,
        coffeeAmount: Int = 0,
        brewWaterAmount: Int = 0,
        groundSize: CoffeeGrindSize = CoffeeGrindSize.MEDIUM,
        brewingMethod: BrewMethod = BrewMethod.STANDARD
    ): Recipe {
        return Recipe(
            diceTemperature = diceTemperature,
            brewTime = brewTime,
            bloomTime = bloomTime,
            bloomWater = bloomWater,
            coffeeAmount = coffeeAmount,
            brewWaterAmount = brewWaterAmount,
            grindSize = groundSize,
            brewMethod = brewingMethod)
    }
}
