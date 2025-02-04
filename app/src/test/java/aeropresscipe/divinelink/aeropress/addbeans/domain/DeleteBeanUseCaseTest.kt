package aeropresscipe.divinelink.aeropress.addbeans.domain

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.DeleteBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import com.divinelink.aerorecipe.sample.model.BeanSample
import com.google.common.truth.Truth
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class DeleteBeanUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var beanRepository: FakeBeanRepository

  private val bean = BeanSample.ethiopia()

  @Before
  fun setUp() {
    beanRepository = FakeBeanRepository()
  }

  @Test
  fun `successfully remove bean`() = runTest {
    val removeBeanResponse = Result.Success(Unit)

    beanRepository.mockRemoveBeanResult(
      bean = bean,
      beanResult = removeBeanResponse,
    )

    val useCase = DeleteBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean)

    assertEquals(result.data, AddBeanResult.Success)
  }

  @Test
  fun testRemoveBeanFailure() = runTest {
    val removeBeanResponse = Result.Error(
      Exception("Damn it"),
    )

    beanRepository.mockRemoveBeanResult(
      bean = bean,
      beanResult = removeBeanResponse,
    )

    val useCase = DeleteBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean)
    assertEquals(result.data, AddBeanResult.Failure.Unknown)
  }

  @Test
  fun testRemoveBeanLoading() = runTest {
    val removeBeanResponse = Result.Loading

    beanRepository.mockRemoveBeanResult(
      bean = bean,
      beanResult = removeBeanResponse,
    )

    val useCase = DeleteBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean)

    Truth.assertThat(result).isInstanceOf(Result.Error::class.java)
  }
}
