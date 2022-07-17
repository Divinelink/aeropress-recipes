package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.generaterecipe.BrewMethod
import aeropresscipe.divinelink.aeropress.generaterecipe.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeIntents
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRemote
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeState
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.IGenerateRecipeViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.ref.WeakReference

@ExperimentalCoroutinesApi
class GenerateRecipeViewModelTest {
    private lateinit var viewModel: GenerateRecipeViewModel
    private lateinit var viewModelIntent: GenerateRecipeIntents

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private var remote: GenerateRecipeRemote = mock()

    private var repository: GenerateRecipeRepository = GenerateRecipeRepository(remote)

    private fun initViewModel() {
        viewModel = GenerateRecipeViewModel(
            delegate = WeakReference(object :
                IGenerateRecipeViewModel {
                override fun updateState(state: GenerateRecipeState) {
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
    fun `given recipe is brewing, when I forceGenerateRecipe then I expect RefreshRecipeState`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(), isBrewing = true)

        whenever(remote.createNewRecipe()).thenReturn(response)
        // When
        viewModel.forceGenerateRecipe()
        // Then
        assertTrue(viewModel.statesList[1] is GenerateRecipeState.RefreshRecipeState)
        assertEquals(viewModel.statesList[2], GenerateRecipeState.HideResumeButtonState)
    }

    @Test
    fun `given recipe is not brewing, when I forceGenerateRecipe then I expect RefreshRecipeState`() = runTest {
        // Given
        val response = DiceDomain(recipeModel(), isBrewing = false)
        whenever(remote.createNewRecipe()).thenReturn(response)
        // When
        viewModel.forceGenerateRecipe()
        // Then
        assertTrue(viewModel.statesList[1] is GenerateRecipeState.RefreshRecipeState)
        assertEquals(viewModel.statesList[2], GenerateRecipeState.HideResumeButtonState)
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
