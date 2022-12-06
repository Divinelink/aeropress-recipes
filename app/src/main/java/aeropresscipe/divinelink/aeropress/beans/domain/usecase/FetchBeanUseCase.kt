package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * This [FetchBeanUseCase] is used to fetch a [Bean] from the [BeanRepository].
 */

class FetchBeanUseCase @Inject constructor(
    // private val beanRepository: BeanRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Bean, Bean>(dispatcher) {
    override suspend fun execute(parameters: Bean): Bean {
        TODO()
//        val result = beanRepository.fetchBean(parameters)
//
//        return when (result) {
//            is Result.Success -> result.data
//            is Result.Error -> throw result.exception
//            Result.Loading -> throw IllegalStateException()
//        }
    }
}
