package aeropresscipe.divinelink.aeropress.beans.domain.repository

import aeropresscipe.divinelink.aeropress.base.data.local.bean.PersistableBean
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.fakes.dao.FakeBeanDAO
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class RoomBeanRepositoryTest {

  val bean = Bean(
    id = "1",
    name = "Bean 1",
    roasterName = "",
    origin = "",
    roastDate = LocalDate.of(1995, 12, 31),
    roastLevel = RoastLevel.Dark,
    process = ProcessMethod.Natural,
    rating = 0,
    tastingNotes = "Full bodied by kinda rotteny after taste.",
    additionalNotes = "",
  )

  private val persistableBean = PersistableBean(
    id = "1",
    name = "Bean 1",
    roasterName = "",
    origin = "",
    roastDate = "1995-12-31",
    roastLevel = "Dark",
    process = "Natural",
    rating = 0,
    tastingNotes = "Full bodied by kinda rotteny after taste.",
    additionalNotes = "",
  )

  private var beanDAO = FakeBeanDAO()

  private lateinit var repository: BeanRepository

  @Before
  fun setUp() {
    repository = RoomBeanRepository(beanDAO.mock)
  }

  @Test
  fun testFetchAllBeans() = runTest {
    val expectedResult = listOf(
      bean,
      bean.copy(id = "2", name = "Bean 2"),
    )

    val expectedPersistableBeanResult = flowOf(
      listOf(
        persistableBean,
        persistableBean.copy(id = "2", name = "Bean 2"),
      ),
    )

    beanDAO.mockFetchAllBeans(expectedPersistableBeanResult)

    val actualResult = repository.fetchAllBeans().first() as Result.Success

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun testAddBean() = runTest {
    // Act
    repository.addBean(bean)

    // Assert
    beanDAO.verifyInsertBean(persistableBean)
  }

  @Test
  fun testFetchBean() = runTest {
    val expectedResult = bean

    beanDAO.mockFetchBeanById("1", persistableBean)

    val actualResult = repository.fetchBean(expectedResult) as Result.Success

    // Assert
    assertEquals(expectedResult, actualResult.data)
  }

  //
  @Test
  fun testUpdateBean() = runTest {
    // Act
    repository.updateBean(bean)

    // Assert
    beanDAO.verifyUpdateBean(persistableBean)
  }

  @Test
  fun testRemoveBean() = runTest {
    // Act
    repository.removeBean(bean)

    // Assert
    beanDAO.verifyRemoveBean(bean.id)
  }
}
