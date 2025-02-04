package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import com.divinelink.aerorecipe.sample.model.BeanSample
import com.divinelink.aerorecipe.sample.model.GroupedCoffeeBeansSample
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class FetchAllBeansUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var beanRepository: FakeBeanRepository

  @Before
  fun setUp() {
    beanRepository = FakeBeanRepository()
  }

  @Test
  fun `given success result when fetchAllBeans then I expect success data`() = runTest {
    beanRepository.mockFetchAllBeansResult(
      response = Result.Success(BeanSample.all()),
    )

    val useCase = FetchAllBeansUseCase(
      beanRepository = beanRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(Unit)

    assertThat(result.first()).isEqualTo(
      Result.Success(GroupedCoffeeBeansSample.group()),
    )
  }

  @Test
  fun `given fail result when fetchAllBeans then I expect exception`() = runTest {
    val expectedResult = Result.Error(
      Exception("No data"),
    )

    beanRepository.mockFetchAllBeansResult(
      response = expectedResult,
    )

    val useCase = FetchAllBeansUseCase(
      beanRepository = beanRepository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(Unit)

    assertEquals(expectedResult, result.first())
  }
}
