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
        roastDate = "",
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
            roastDate = "",
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
            .assertViewState(AddBeanViewState())
    }

    @Test
    fun `onSetBean successfully sets bean on ViewState`() = runTest {
        testRobot
            .buildViewModel()
            .onSetBean(testBean)
            .assertViewState(
                AddBeanViewState(
                    bean = testBean,
                    showLoading = false
                )
            )
    }

    @Test
    fun `onBeanNameChanged successfully updates name`() = runTest {
        testRobot
            .buildViewModel()
            .onBeanNameChanged("name changed")
            .assertViewState(
                AddBeanViewState(
                    bean = emptyBean().copy(name = "name changed"),
                    showLoading = false
                )
            )
    }

    @Test
    fun `onRoasterNameChanged successfully updates roaster name`() = runTest {
        testRobot
            .buildViewModel()
            .onRoasterNameChanged("roaster name changed")
            .assertViewState(
                AddBeanViewState(
                    bean = emptyBean().copy(roasterName = "roaster name changed"),
                    showLoading = false
                )
            )
    }

    @Test
    fun `onOriginChanged successfully updates origin name`() = runTest {
        testRobot
            .buildViewModel()
            .onOriginChanged("origin name changed")
            .assertViewState(
                AddBeanViewState(
                    bean = emptyBean().copy(origin = "origin name changed"),
                    showLoading = false
                )
            )
    }

    @Test
    fun `onDateChanged successfully updates date`() = runTest {
        testRobot
            .buildViewModel()
            .onDateChanged(LocalDate.now())
            .assertViewState(
                AddBeanViewState(
                    bean = emptyBean().copy(roastDate = LocalDate.now().toString()),
                    showLoading = false
                )
            )
    }

    @Test
    fun `onRoastLevelChanged successfully updates roast level`() = runTest {
        testRobot
            .buildViewModel()
            .onRoastLevelChanged(RoastLevel.Medium.name)
            .assertViewState(
                AddBeanViewState(
                    bean = emptyBean().copy(roastLevel = RoastLevel.Medium),
                    showLoading = false
                )
            )
    }

    @Test
    fun `onProcessChanged successfully updates process method`() = runTest {
        testRobot
            .buildViewModel()
            .onProcessChanged(ProcessMethod.Natural.name)
            .assertViewState(
                AddBeanViewState(
                    bean = emptyBean().copy(process = ProcessMethod.Natural),
                    showLoading = false
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
