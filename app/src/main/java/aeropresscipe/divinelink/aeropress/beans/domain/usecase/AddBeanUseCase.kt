package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * The [AddBeanUseCase] is used to store a [Bean] model into the Local DB using the [BeanRepository].
 * Returns true if [Result.Success], false otherwise.
 */
class AddBeanUseCase @Inject constructor(
    private val beanRepository: BeanRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Bean, Unit>(dispatcher) {
    override suspend fun execute(parameters: Bean) {
        val result = beanRepository.addBean(parameters)

        return when (result) {
            is Result.Success -> result.data
            is Result.Error -> throw result.exception
            Result.Loading -> throw IllegalStateException()
        }
    }
}
