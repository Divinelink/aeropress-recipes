package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test

@Suppress("IllegalIdentifier")
@OptIn(ExperimentalCoroutinesApi::class)
class AddBeanViewModelTest {

    private val testRobot = AddBeanViewModelRobot()

    private val testBean = Bean(
        id = UUID.randomUUID().toString(),
        name = "testBean",
        roasterName = "testRoaster",
        origin = "",
        roastDate = null,
        roastLevel = null,
        process = null,
        rating = 0,
        tastingNotes = "nothing",
        additionalNotes = "empty notes"
    )

    private fun emptyBean(): Bean {
        return Bean(
            id = "",
            name = "",
            roasterName = "",
            origin = "",
            roastDate = null,
            roastLevel = null,
            process = null,
            rating = 0,
            tastingNotes = "",
            additionalNotes = ""
        )
    }

    @Test
    fun initialiseViewModelTest() = runTest {
        testRobot
            .buildViewModel()
            .assertViewState(AddBeanViewState.Initial)
    }

    @Test
    fun `onSetBean successfully sets UpdateBean state`() = runTest {
        testRobot
            .buildViewModel()
            .onSetBean(testBean)
            .assertViewState(
                AddBeanViewState.UpdateBean(
                    bean = testBean,
                )
            )
    }

    @Test
    fun `onBeanNameChanged successfully updates name`() = runTest {
        testRobot
            .buildViewModel()
            .onSetBean(null)
            .onBeanNameChanged("name changed")
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(name = "name changed"),
                )
            )
    }

    @Test
    fun `given Initial state when updating any bean parameter then I expect InsertBean state`() = runTest {
        testRobot
            .buildViewModel()
            .onBeanNameChanged("name changed")
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(name = "name changed"),
                )
            )
    }

    @Test
    fun `onRoasterNameChanged successfully updates roaster name`() = runTest {
        testRobot
            .buildViewModel()
            .onRoasterNameChanged("roaster name changed")
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(roasterName = "roaster name changed"),
                )
            )
    }

    @Test
    fun `onOriginChanged successfully updates origin name`() = runTest {
        testRobot
            .buildViewModel()
            .onOriginChanged("origin name changed")
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(origin = "origin name changed"),
                )
            )
    }

    @Test
    fun `onDateChanged successfully updates date`() = runTest {
        testRobot
            .buildViewModel()
            .onDateChanged(LocalDate.now())
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(roastDate = LocalDate.now()),
                )
            )
    }

    @Test
    fun `onRoastLevelChanged successfully updates roast level`() = runTest {
        testRobot
            .buildViewModel()
            .onRoastLevelChanged(RoastLevel.Medium.name)
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(roastLevel = RoastLevel.Medium),
                )
            )
    }

    @Test
    fun `onProcessChanged successfully updates process method`() = runTest {
        testRobot
            .buildViewModel()
            .onProcessChanged(ProcessMethod.Natural.name)
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = emptyBean().copy(process = ProcessMethod.Natural),
                )
            )
    }

    @Test
    fun `successfully update many bean fields`() = runTest {
        testRobot
            .buildViewModel()
            .assertViewState(AddBeanViewState.Initial)
            .onProcessChanged(ProcessMethod.Natural.name)
            .onRoastLevelChanged(RoastLevel.Medium.name)
            .onBeanNameChanged("Guji")
            .onOriginChanged("Ethiopia")
            .onRoasterNameChanged("Omsom Roastery")
            .assertViewState(
                AddBeanViewState.InsertBean(
                    bean = Bean(
                        id = "",
                        name = "Guji",
                        roasterName = "Omsom Roastery",
                        origin = "Ethiopia",
                        roastDate = null,
                        roastLevel = RoastLevel.Medium,
                        process = ProcessMethod.Natural,
                        rating = 0,
                        tastingNotes = "",
                        additionalNotes = ""
                    ),
                )
            )
    }
    //    fun onRoastLevelClicked() = apply {
    //        viewModel.onRoastLevelClicked()
    //    }
    //
    //    fun onProcessClicked() = apply {
    //        viewModel.onProcessClicked()
    //    }
    //
    //    fun onAddBean(bean: Bean) = apply {
    //        viewModel.addBean(bean)
    //    }
}
