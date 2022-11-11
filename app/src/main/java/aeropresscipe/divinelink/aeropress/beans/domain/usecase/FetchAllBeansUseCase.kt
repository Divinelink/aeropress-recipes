package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FetchAllBeansUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Unit, List<Bean>>(dispatcher) {
    override suspend fun execute(parameters: Unit): List<Bean> {
        TODO()
    }
}
