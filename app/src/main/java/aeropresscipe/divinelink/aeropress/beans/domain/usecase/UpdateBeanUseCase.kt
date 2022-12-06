package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateBeanUseCase @Inject constructor(
    // private val beanRepository: BeanRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Bean, Bean>(dispatcher) {
    override suspend fun execute(parameters: Bean): Bean {
        TODO()
    }
}
