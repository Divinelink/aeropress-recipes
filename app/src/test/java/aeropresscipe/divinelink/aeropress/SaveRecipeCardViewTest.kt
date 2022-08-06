package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.customviews.ISaveRecipeCardViewModel
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardIntents
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardState
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardView.Companion.LIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.models.BrewMethod
import aeropresscipe.divinelink.aeropress.generaterecipe.models.CoffeeGrindSize
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.ref.WeakReference


@ExperimentalCoroutinesApi
class SaveRecipeCardViewTest {
    private lateinit var viewModel: SaveRecipeCardViewModel
    private lateinit var viewModelIntent: SaveRecipeCardIntents

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private var remote: TimerServices = mock()

    private var repository: TimerRepository = TimerRepository(remote)

    private fun initViewModel() {
        viewModel = SaveRecipeCardViewModel(
            delegate = WeakReference(object :
                ISaveRecipeCardViewModel {
                override fun updateState(state: SaveRecipeCardState) {
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
    fun `given recipe is liked, on init return saved state`() = runTest {
        val response = recipeModel()
        whenever(remote.isRecipeSaved(response)).thenReturn(true)

        viewModel.init(recipeModel())

        Assert.assertEquals(viewModel.state, SaveRecipeCardState.UpdateSavedIndicator(LIKE_MAX_FRAME))
    }

    @Test
    fun `given recipe is not liked, on init return saved state`() = runTest {
        val response = recipeModel()

        whenever(remote.isRecipeSaved(response)).thenReturn(false)

        viewModel.init(response)

        Assert.assertEquals(viewModel.state, SaveRecipeCardState.UpdateSavedIndicator(DISLIKE_MAX_FRAME))
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
