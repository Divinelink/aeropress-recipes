package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class UpdateBeanUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var beanRepository: FakeBeanRepository

    private val bean = Bean(
        id = "0",
        name = "beanName",
        roasterName = "roasterName",
        origin = "originName",
        roastDate = LocalDate.now(),
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
    fun testUpdateBeanSuccessfully() = runTest {
        val addBeanResponse = Result.Success(Unit)

        beanRepository.mockUpdateBeanResult(
            bean = bean,
            beanResult = addBeanResponse
        )

        val useCase = UpdateBeanUseCase(beanRepository.mock, testDispatcher)
        val result = useCase(bean)

        assertThat(result).isEqualTo(Result.Success(Unit))
    }

    @Test
    fun testUpdateBeanFailure() = runTest {
        val addBeanResponse = Result.Error(
            Exception("Damn it")
        )

        beanRepository.mockUpdateBeanResult(
            bean = bean,
            beanResult = addBeanResponse
        )

        val useCase = UpdateBeanUseCase(beanRepository.mock, testDispatcher)
        val result = useCase(bean)
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun testUpdateBeanLoading() = runTest {
        val addBeanResponse = Result.Loading

        beanRepository.givenAddBeanResult(
            bean = bean,
            beanResult = addBeanResponse
        )

        val useCase = UpdateBeanUseCase(beanRepository.mock, testDispatcher)
        val result = useCase(bean)

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}
