package aeropresscipe.divinelink.aeropress.beans.domain.repository

import aeropresscipe.divinelink.aeropress.base.data.local.bean.BeanDAO
import aeropresscipe.divinelink.aeropress.base.data.local.bean.PersistableBean
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import aeropresscipe.divinelink.aeropress.extension.currentEpochSeconds
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RoomBeanRepository @Inject constructor(
  private val beanDAO: BeanDAO,
  private val clock: Clock,
) : BeanRepository {

  override fun fetchAllBeans(): Flow<BeanListResult> = beanDAO
    .fetchAllBeans()
    .map { beansList ->
      Result.Success(beansList.toDomainBeansList())
    }

  override suspend fun addBean(bean: Bean): Result<Unit> {
    beanDAO
      .insertBean(bean.toPersistableBean(clock.currentEpochSeconds()))
      .also {
        return Result.Success(Unit)
      }
  }

  override suspend fun fetchBean(id: String): Result<Bean> {
    beanDAO
      .fetchBeanById(id)
      .toBean()
      .also {
        return Result.Success(it)
      }
  }

  override suspend fun updateBean(bean: Bean): Result<Unit> {
    beanDAO
      .updateBean(bean.toPersistableBean(bean.timestamp))
      .also {
        return Result.Success(it)
      }
  }

  override suspend fun removeBean(bean: Bean): Result<Unit> {
    beanDAO
      .removeBean(bean.id)
      .also {
        return Result.Success(it)
      }
  }
}

private fun List<PersistableBean>.toDomainBeansList(): List<Bean> =
  this.map(PersistableBean::toBean)

private const val PERSISTED_DATE_FORMAT = "yyyy-MM-dd"
private val persistedDateFormatter = DateTimeFormatter.ofPattern(PERSISTED_DATE_FORMAT)

private fun LocalDate.toPersistableDateString(): String = persistedDateFormatter.format(this)

private fun PersistableBean.toBean(): Bean {
  val date: LocalDate? = this.roastDate?.let { date ->
    LocalDate.parse(date, persistedDateFormatter).takeIf {
      it is LocalDate
    }
  }
  return Bean(
    id = this.id,
    name = this.name,
    roasterName = this.roasterName,
    origin = this.origin,
    roastDate = date,
    roastLevel = enumValues<RoastLevel>().find { it.name == this.roastLevel },
    process = enumValues<ProcessMethod>().find { it.name == this.process },
    rating = this.rating,
    tastingNotes = this.tastingNotes,
    additionalNotes = this.additionalNotes,
    timestamp = this.timestamp,
  )
}

private fun Bean.toPersistableBean(timestamp: String): PersistableBean = PersistableBean(
  id = this.id,
  name = this.name,
  roasterName = this.roasterName,
  origin = this.origin,
  roastDate = this.roastDate?.toPersistableDateString(),
  roastLevel = this.roastLevel.toString(),
  process = this.process.toString(),
  rating = this.rating,
  tastingNotes = this.tastingNotes,
  additionalNotes = this.additionalNotes,
  timestamp = timestamp,
)
