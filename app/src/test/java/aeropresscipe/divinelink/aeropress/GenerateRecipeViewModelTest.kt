package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeIntents
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRemote
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeState
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.IGenerateRecipeViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
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
    fun `given recipe is brewing, when I getRecipe then I expect ShowResumeButtonState`() = runTest {
        // Given
        val response = DiceDomain(
            Recipe(diceTemperature = 0,
                brewTime = 0,
                bloomTime = 0,
                bloomWater = 0,
                coffeeAmount = 0,
                brewWaterAmount = 0,
                groundSize = "",
                brewingMethod = "",
                isNewRecipe = false), isBrewing = true)

        whenever(remote.createNewRecipe()).thenReturn(response)
        // When
        viewModel.forceGenerateRecipe()
        // Then
        assertTrue(viewModel.statesList[1] is GenerateRecipeState.RefreshRecipeState)
        assertEquals(viewModel.statesList[2], GenerateRecipeState.HideResumeButtonState)

    }
}
