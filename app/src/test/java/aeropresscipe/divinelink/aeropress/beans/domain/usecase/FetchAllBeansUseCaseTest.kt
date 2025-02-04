package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.MainDispatcherRule
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.fakes.FakeBeanRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
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
      roastDate = LocalDate.now(),
    )
  }.toMutableList()

  @Test
  fun `given success result when fetchAllBeans then I expect success data`() = runTest {
    val expectedResult = Result.Success<List<Bean>>(
      beans,
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
