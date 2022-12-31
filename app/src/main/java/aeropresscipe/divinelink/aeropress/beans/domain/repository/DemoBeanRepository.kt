package aeropresscipe.divinelink.aeropress.beans.domain.repository

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DemoBeanRepository @Inject constructor() : BeanRepository {

    @Suppress("MagicNumber")
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
            roastDate = null,
        )
    }.toMutableList()

    private val beansFlow = MutableStateFlow(beans)

    override fun fetchAllBeans(): Flow<BeanListResult> {
        return beansFlow.map { beans ->
            Result.Success(beans)
        }
    }

    override suspend fun addBean(bean: Bean): Result<Unit> {
        beans.add(0, bean)

        beansFlow.value = beans

        return Result.Success(Unit)
    }

    override suspend fun fetchBean(bean: Bean): Result<Bean> {
        //        TODO("Not yet implemented")
        return Result.Success(bean)
    }

    override suspend fun updateBean(bean: Bean): Result<Unit> {
        // TODO("Not yet implemented")
        return Result.Success(Unit)
    }

    override suspend fun removeBean(bean: Bean): Result<Unit> {
        beans.remove(bean)

        beansFlow.value = beans

        return Result.Success(Unit)
    }
}
