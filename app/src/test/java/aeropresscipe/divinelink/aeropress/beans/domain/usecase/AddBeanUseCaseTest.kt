package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import com.divinelink.aerorecipe.sample.model.BeanSample
import com.google.common.truth.Truth.assertThat
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

  private val bean = BeanSample.ethiopia()

  @Before
  fun setUp() {
    beanRepository = FakeBeanRepository()
  }

  @Test
  fun testAddBeanSuccessfully() = runTest {
    val addBeanResponse = Result.Success(Unit)

    beanRepository.givenAddBeanResult(
      bean = bean,
      beanResult = addBeanResponse,
    )

    val useCase = AddBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean)

    assertTrue(result.data == AddBeanResult.Success)
  }

  @Test
  fun testAddBeanFailure() = runTest {
    val addBeanResponse = Result.Error(
      Exception("Damn it"),
    )

    beanRepository.givenAddBeanResult(
      bean = bean,
      beanResult = addBeanResponse,
    )

    val useCase = AddBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean)
    assertTrue(result.data == AddBeanResult.Failure.Unknown)
  }

  @Test
  fun testAddBeanLoading() = runTest {
    val addBeanResponse = Result.Loading

    beanRepository.givenAddBeanResult(
      bean = bean,
      beanResult = addBeanResponse,
    )

    val useCase = AddBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean)

    assertThat(result).isInstanceOf(Result.Error::class.java)
  }

  @Test
  fun `on empty bean name I expect EmptyName Error`() = runTest {
    val addBeanResponse = Result.Error(
      Exception("Empty Name"),
    )

    beanRepository.givenAddBeanResult(
      bean = bean.copy(name = ""),
      beanResult = addBeanResponse,
    )

    val useCase = AddBeanUseCase(beanRepository.mock, testDispatcher)
    val result = useCase(bean.copy(name = ""))

    assertThat(result.data).isEqualTo(AddBeanResult.Failure.EmptyName)
  }
}
