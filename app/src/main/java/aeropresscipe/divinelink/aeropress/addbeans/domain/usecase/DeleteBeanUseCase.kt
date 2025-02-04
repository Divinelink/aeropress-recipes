package aeropresscipe.divinelink.aeropress.addbeans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@Suppress("ThrowingExceptionsWithoutMessageOrCause")
open class DeleteBeanUseCase @Inject constructor(
  private val beanRepository: BeanRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Bean, AddBeanResult>(dispatcher) {
  override suspend fun execute(parameters: Bean): AddBeanResult {
    val result = beanRepository.removeBean(parameters)

    return when (result) {
      is Result.Success -> AddBeanResult.Success
      is Result.Error -> AddBeanResult.Failure.Unknown
      Result.Loading -> throw IllegalStateException()
    }
  }
}
