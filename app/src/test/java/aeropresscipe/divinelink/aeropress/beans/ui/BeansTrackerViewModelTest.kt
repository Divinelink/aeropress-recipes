package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BeansTrackerViewModelTest {

    private val testRobot = BeansTrackerViewModelRobot()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun successfulInit() = runTest {
        testRobot
            .buildViewModel()
            .assertViewState(BeanTrackerViewState.Initial)
    }

    @Test
    fun `given empty beans list when I init then I expect Completed State with empty list`() = runTest {
        testRobot
            .mockFetchAllBeans(
                Result.Success(emptyList())
            )
            .buildViewModel()
            .assertViewState(
                BeanTrackerViewState.Completed(listOf())
            )
    }

    @Test
    fun `given a beans list when I init then I expect Completed State`() = runTest {
        testRobot
            .mockFetchAllBeans(
                response = Result.Success(beans)
            )
            .buildViewModel()
            .assertViewState(
                expectedViewState = BeanTrackerViewState.Completed(beans)
            )
    }

    private val beans = (1..10).map { index ->
        Bean(
            id = index.toString(),
            name = "Bean name $index",
            roasterName = "Roaster name $index",
            origin = "Origin $index",
            roastLevel = RoastLevel.Dark,
            process = ProcessMethod.Honey,
            rating = 0,
            tastingNotes = "",
            additionalNotes = "",
            roastDate = ""
        )
    }.toMutableList()
}
