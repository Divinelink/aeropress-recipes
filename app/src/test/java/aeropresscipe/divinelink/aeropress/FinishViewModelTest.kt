package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.finish.FinishIntents
import aeropresscipe.divinelink.aeropress.finish.FinishState
import aeropresscipe.divinelink.aeropress.finish.FinishViewModel
import aeropresscipe.divinelink.aeropress.finish.IFinishViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRemote
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.mock
import java.lang.ref.WeakReference
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class FinishViewModelTest {
    private lateinit var viewModel: FinishViewModel
    private lateinit var viewModelIntent: FinishIntents

    @Mock
    private var remote: GenerateRecipeRemote = mock()
    private var repository: GenerateRecipeRepository = GenerateRecipeRepository(remote)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun initViewModel() {
        viewModel = FinishViewModel(
            delegate = WeakReference(object :
                IFinishViewModel {
                override fun updateState(state: FinishState) {
                    // do nothing
                }
            }), repository = repository)

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
        assertTrue(viewModel.statesList[0] is FinishState.InitialState)
    }

    @Test
    fun `When close button clicked then I expect Close State`() {
        viewModel.closeButtonClicked()
        assertTrue(viewModel.statesList[0] is FinishState.InitialState)
        assertTrue(viewModel.statesList[1] is FinishState.CloseState)
    }

}