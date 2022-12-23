package aeropresscipe.divinelink.aeropress.beans.domain.repository

import aeropresscipe.divinelink.aeropress.base.data.local.bean.BeanDAO
import aeropresscipe.divinelink.aeropress.base.data.local.bean.PersistableBean
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomBeanRepository @Inject constructor(
    private val beanDAO: BeanDAO,
) : BeanRepository {

    override fun fetchAllBeans(): Flow<BeanListResult> {
        return beanDAO
            .fetchAllBeans()
            .map { beansList ->
                Result.Success(beansList.toDomainBeansList())
            }
    }

    override suspend fun addBean(bean: Bean): Result<Unit> {
        beanDAO.insertBean(bean.toPersistableBean())

        return Result.Success(Unit)
    }

    override suspend fun fetchBean(bean: Bean): Result<Bean> {
        beanDAO
            .fetchBeanById(bean.id)
            .toBean()
            .also {
                return Result.Success(it)
            }
    }

    override suspend fun updateBean(bean: Bean): Result<Unit> {
        beanDAO
            .updateTask(bean.toPersistableBean())
            .also {
                return Result.Success(it)
            }
    }
}

private fun List<PersistableBean>.toDomainBeansList(): List<Bean> {
    return this.map(PersistableBean::toBean)
}

private fun PersistableBean.toBean(): Bean {
    return Bean(
        id = this.id,
        name = this.name,
        roasterName = this.roasterName,
        origin = this.origin,
        roastDate = this.roastDate,
        roastLevel = enumValues<RoastLevel>().find { it.name == this.roasterName },
        process = enumValues<ProcessMethod>().find { it.name == this.process },
        rating = this.rating,
        tastingNotes = this.tastingNotes,
        additionalNotes = this.additionalNotes
    )
}

private fun Bean.toPersistableBean(): PersistableBean {
    return PersistableBean(
        id = this.id,
        name = this.name,
        roasterName = this.roasterName,
        origin = this.origin,
        roastDate = this.roastDate,
        roastLevel = this.roastLevel.toString(),
        process = this.process.toString(),
        rating = this.rating,
        tastingNotes = this.tastingNotes,
        additionalNotes = this.additionalNotes
    )
}
