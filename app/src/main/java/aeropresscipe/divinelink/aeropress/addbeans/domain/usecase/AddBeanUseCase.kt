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
    val bean = parameters.trimFields()
    if (bean.name.isEmpty()) {
      return AddBeanResult.Failure.EmptyName
    }
    val result = beanRepository.addBean(bean)

    return when (result) {
      is Result.Success -> AddBeanResult.Success
      is Result.Error -> AddBeanResult.Failure.Unknown
      Result.Loading -> throw IllegalStateException()
    }
  }
}

internal fun Bean.trimFields(): Bean {
  return this.copy(
    name = this.name.trim(),
    roasterName = this.roasterName.trim(),
    origin = this.origin.trim(),
    tastingNotes = this.tastingNotes.trim(),
    additionalNotes = this.additionalNotes.trim(),
  )
}
