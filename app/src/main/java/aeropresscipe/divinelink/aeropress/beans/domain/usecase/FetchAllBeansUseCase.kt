package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanListResult
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchAllBeansUseCase @Inject constructor(
  private val beanRepository: BeanRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<Bean>>(dispatcher) {

  override fun execute(
    parameters: Unit,
  ): Flow<BeanListResult> {
    return beanRepository.fetchAllBeans().map { result ->
      when (result) {
        is Result.Success -> result
        is Result.Error -> result
        Result.Loading -> result
      }
    }
  }
}
