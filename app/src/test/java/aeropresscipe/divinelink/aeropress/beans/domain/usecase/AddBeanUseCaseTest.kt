package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.data
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddBeanUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var beanRepository: FakeBeanRepository

    private val bean = Bean(
        id = "0",
        name = "beanName",
        roasterName = "roasterName",
        origin = "originName",
        roastDate = "12/12/22",
        roastLevel = RoastLevel.Dark,
        process = ProcessMethod.Honey,
        rating = 0,
        tastingNotes = "",
        additionalNotes = ""
    )

    @Before
    fun setUp() {
        beanRepository = FakeBeanRepository()
    }

    @Test
    fun testAddBeanSuccessfully() = runTest {
        val addBeanResponse = Result.Success(Unit)

        beanRepository.givenAddBeanResult(
            bean = bean,
            beanResult = addBeanResponse
        )

        val useCase = AddBeanUseCase(beanRepository.mock, testDispatcher)
        val result = useCase(bean)

        assertTrue(result.data == AddBeanResult.Success)
    }

    @Test
    fun testAddBeanFailure() = runTest {
        val addBeanResponse = Result.Error(
            Exception("Damn it")
        )

        beanRepository.givenAddBeanResult(
            bean = bean,
            beanResult = addBeanResponse
        )

        val useCase = AddBeanUseCase(beanRepository.mock, testDispatcher)
        val result = useCase(bean)
        assertTrue(result.data == AddBeanResult.Failure)
    }
}
