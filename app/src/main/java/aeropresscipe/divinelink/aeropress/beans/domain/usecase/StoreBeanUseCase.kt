package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * The [StoreBeanUseCase] is used to store a [Bean] model into the Local DB using the [BeanRepository].
 * Returns true if [Result.Success], false otherwise.
 */
class StoreBeanUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Bean, Boolean>(dispatcher) {
    override suspend fun execute(parameters: Bean): Boolean {
        TODO()
    }
}
