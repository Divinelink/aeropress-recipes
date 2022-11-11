package aeropresscipe.divinelink.aeropress.beans.domain.repository

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class DemoBeanRepository @Inject constructor() : BeanRepository {

    override suspend fun fetchAllBeans(): BeanListResult {
        @Suppress("MagicNumber")
        delay(2_000)
        val beans = (1..10).map { index ->
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
                roastDate = ""
            )
        }
        return Result.Success(beans)
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
