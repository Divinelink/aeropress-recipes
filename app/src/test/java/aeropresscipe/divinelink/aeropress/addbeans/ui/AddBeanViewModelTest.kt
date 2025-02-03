package aeropresscipe.divinelink.aeropress.addbeans.ui

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.ui.UIText
import com.divinelink.aeropress.recipes.R
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddBeanViewModelTest {

    private val testRobot = AddBeanViewModelRobot()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

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
                AddBeanViewState.ModifyBean(
                    bean = testBean,
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    withDeleteAction = true,
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
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(name = "name changed"),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `given Initial state when updating any bean parameter then I expect InsertBean state`() = runTest {
        testRobot
            .buildViewModel()
            .onBeanNameChanged("name changed")
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(name = "name changed"),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `onRoasterNameChanged successfully updates roaster name`() = runTest {
        testRobot
            .buildViewModel()
            .onRoasterNameChanged("roaster name changed")
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(roasterName = "roaster name changed"),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `onOriginChanged successfully updates origin name`() = runTest {
        testRobot
            .buildViewModel()
            .onOriginChanged("origin name changed")
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(origin = "origin name changed"),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `onDateChanged successfully updates date`() = runTest {
        testRobot
            .buildViewModel()
            .onDateChanged(LocalDate.now())
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(roastDate = LocalDate.now()),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `onRoastLevelChanged successfully updates roast level`() = runTest {
        testRobot
            .buildViewModel()
            .onSelectFromBottomSheet(RoastLevel.Medium.name)
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(roastLevel = RoastLevel.Medium),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true
                )
            )
    }

    @Test
    fun `onProcessChanged successfully updates process method`() = runTest {
        testRobot
            .buildViewModel()
            .onSelectFromBottomSheet(ProcessMethod.Natural.name)
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = emptyBean().copy(process = ProcessMethod.Natural),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `successfully update many bean fields`() = runTest {
        testRobot
            .buildViewModel()
            .assertViewState(AddBeanViewState.Initial)
            .onSelectFromBottomSheet(ProcessMethod.Natural.name)
            .onSelectFromBottomSheet(RoastLevel.Medium.name)
            .onBeanNameChanged("Guji")
            .onOriginChanged("Ethiopia")
            .onRoasterNameChanged("Omsom Roastery")
            .assertViewState(
                AddBeanViewState.ModifyBean(
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
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    submitButtonText = UIText.ResourceText(R.string.save),
                    isSubmitButtonEnabled = true,
                )
            )
    }

    @Test
    fun `given empty bean when submit clicked then I expect random UUID`() = runTest {
        val response: Result<AddBeanResult> = Result.Success(data = AddBeanResult.Success)
        testRobot
            .mockAddBeanResult(
                response = response
            )
            .buildViewModel()
            .onBeanNameChanged("update name")
            .onSubmitClicked()
            .assertViewState(
                AddBeanViewState.Completed(
                    submitButtonText = UIText.ResourceText(R.string.save),
                    title = UIText.ResourceText(R.string.AddBeans__add_title),
                    withDeleteAction = false,
                )
            )
    }

    @Test
    fun `given a random bean when submit clicked then I expect update use case with Completed State`() = runTest {
        val successResult: Result<AddBeanResult> = Result.Success(AddBeanResult.Success)
        testRobot
            .mockAddBeanResult(successResult)
            .mockUpdateBeanResult(successResult)
            .buildViewModel()
            .onSetBean(testBean)
            .onSubmitClicked()
            .onBeanNameChanged("update name")
            .onSubmitClicked()
            .assertViewState(
                AddBeanViewState.Completed(
                    submitButtonText = UIText.ResourceText(R.string.update),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    withDeleteAction = true,
                )
            )
            .assertFalseViewState(
                AddBeanViewState.Error(
                    bean = testBean.copy(name = "update name"),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    error = AddBeanResult.Failure.Unknown,
                    withDeleteAction = true,
                )
            )
    }

    @Test
    fun `given fail result when submit then I expect error state`() = runTest {
        testRobot
            .mockUpdateBeanResult(Result.Error(Exception()))
            .buildViewModel()
            .onSetBean(testBean)
            .onSubmitClicked()
            .assertViewState(
                AddBeanViewState.Error(
                    bean = testBean,
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    error = AddBeanResult.Failure.Unknown,
                    withDeleteAction = true,
                )
            )
            .assertFalseViewState(
                AddBeanViewState.Completed(
                    submitButtonText = UIText.ResourceText(R.string.update),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    withDeleteAction = true,
                )
            )
    }

    @Test
    fun `given empty name result data when submit then I expect error state`() = runTest {
        testRobot
            .mockUpdateBeanResult(Result.Success(AddBeanResult.Failure.EmptyName))
            .buildViewModel()
            .onSetBean(testBean.copy(name = ""))
            .onSubmitClicked()
            .assertViewState(
                AddBeanViewState.Error(
                    bean = testBean.copy(name = ""),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    error = AddBeanResult.Failure.EmptyName,
                    withDeleteAction = true,
                )
            )
            .assertFalseViewState(
                AddBeanViewState.Completed(
                    submitButtonText = UIText.ResourceText(R.string.update),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    withDeleteAction = true,
                )
            )
    }

    @Test
    fun `given success state when I delete bean clicked then I expect Completed State`() = runTest {
        testRobot
            .mockDeleteBeanResult(Result.Success(AddBeanResult.Success))
            .buildViewModel()
            .onSetBean(testBean)
            .onDeleteClicked()
            .assertViewState(
                AddBeanViewState.Completed(
                    submitButtonText = UIText.ResourceText(R.string.update),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    withDeleteAction = true,
                )
            )
    }

    @Test
    fun `given error when delete bean clicked then I expect Unknown Error State`() = runTest {
        testRobot
            .mockDeleteBeanResult(Result.Error(Exception()))
            .buildViewModel()
            .onSetBean(testBean)
            .onDeleteClicked()
            .assertViewState(
                AddBeanViewState.Error(
                    bean = testBean,
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    error = AddBeanResult.Failure.Unknown,
                    withDeleteAction = true,
                )
            )
    }

    @Test
    fun `when roastLevelClicked then I expect BottomSheet content with all of RoastLevel values`() = runTest {
        val roastLevelContent = RoastLevel.values().map { roastLevel ->
            UIText.StringText(roastLevel.name)
        }.toMutableList()

        testRobot
            .buildViewModel()
            .onSetBean(testBean)
            .onRoastLevelClicked()
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = testBean,
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    withDeleteAction = true,
                    bottomSheetContent = roastLevelContent,
                    bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_roast_level),
                )
            )
    }

    @Test
    fun `given a selected option for RoastLevel, when roastLevelClicked, then I expect BottomSheet content with selected option`() = runTest {
        val roastLevelContent = RoastLevel.values().map { roastLevel ->
            UIText.StringText(roastLevel.name)
        }.toMutableList()

        testRobot
            .buildViewModel()
            .onSetBean(testBean)
            .onSelectFromBottomSheet(RoastLevel.Medium.name)
            .onRoastLevelClicked()
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = testBean.copy(roastLevel = RoastLevel.Medium),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    withDeleteAction = true,
                    bottomSheetContent = roastLevelContent,
                    bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_roast_level),
                    bottomSheetSelectedOption = UIText.StringText(RoastLevel.Medium.name)
                )
            )
    }

    @Test
    fun `when processClicked then I expect BottomSheet content with all of ProcessMethod values`() = runTest {
        val processMethodContent = ProcessMethod.values().map { process ->
            UIText.ResourceText(process.stringRes)
        }.toMutableList()

        testRobot
            .buildViewModel()
            .onSetBean(testBean)
            .onProcessClicked()
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = testBean,
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    withDeleteAction = true,
                    bottomSheetContent = processMethodContent,
                    bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_process_method),
                )
            )
    }

    @Test
    fun `given a selected process method, when processClicked then I expect BottomSheet content with selected option`() = runTest {
        val processMethodContent = ProcessMethod.values().map { process ->
            UIText.ResourceText(process.stringRes)
        }.toMutableList()

        testRobot
            .buildViewModel()
            .onSetBean(testBean)
            .onProcessClicked()
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = testBean,
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    withDeleteAction = true,
                    bottomSheetContent = processMethodContent,
                    bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_process_method),
                )
            )
            .onSelectFromBottomSheet(ProcessMethod.CarbonicMaceration.value)
            .onProcessClicked()
            .assertViewState(
                AddBeanViewState.ModifyBean(
                    bean = testBean.copy(process = ProcessMethod.CarbonicMaceration),
                    title = UIText.ResourceText(R.string.AddBeans__update_title),
                    submitButtonText = UIText.ResourceText(R.string.update),
                    withDeleteAction = true,
                    bottomSheetContent = processMethodContent,
                    bottomSheetTitle = UIText.ResourceText(R.string.AddBeans__select_process_method),
                    bottomSheetSelectedOption = UIText.ResourceText(ProcessMethod.CarbonicMaceration.stringRes)
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
