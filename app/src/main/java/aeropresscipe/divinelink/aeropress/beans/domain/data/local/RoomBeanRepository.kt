package aeropresscipe.divinelink.aeropress.beans.domain.data.local

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanListResult
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomBeanRepository @Inject constructor(
//    private val beanDAO: BeanDAO,
) : BeanRepository {

    override fun fetchAllBeans(): Flow<BeanListResult> {
        TODO("Not yet implemented")
    }

    override suspend fun addBean(bean: Bean): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchBean(bean: Bean): Result<Bean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBean(bean: Bean): Result<Unit> {
        TODO("Not yet implemented")
    }
}
