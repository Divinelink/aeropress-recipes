package aeropresscipe.divinelink.aeropress.addbeans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
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
@Suppress("ThrowingExceptionsWithoutMessageOrCause")
open class AddBeanUseCase @Inject constructor(
    private val beanRepository: BeanRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Bean, AddBeanResult>(dispatcher) {
    override suspend fun execute(parameters: Bean): AddBeanResult {
        if (parameters.name.isEmpty()) {
            return AddBeanResult.Failure.EmptyName
        }
        val result = beanRepository.addBean(parameters)

        return when (result) {
            is Result.Success -> AddBeanResult.Success
            is Result.Error -> AddBeanResult.Failure.Unknown
            Result.Loading -> throw IllegalStateException()
        }
    }
}
