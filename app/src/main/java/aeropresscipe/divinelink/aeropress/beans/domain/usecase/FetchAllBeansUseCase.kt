package aeropresscipe.divinelink.aeropress.beans.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.beans.domain.model.GroupedCoffeeBeans
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import aeropresscipe.divinelink.aeropress.extension.formatDate
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchAllBeansUseCase @Inject constructor(
  private val beanRepository: BeanRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, GroupedCoffeeBeans>(dispatcher) {

  override fun execute(parameters: Unit): Flow<Result<GroupedCoffeeBeans>> =
    beanRepository.fetchAllBeans().map { result ->
      when (result) {
        is Result.Success -> {
          val map = result.data.groupBy { it.timestamp.formatDate() }

          Result.Success(GroupedCoffeeBeans(map))
        }
        is Result.Error -> result
        Result.Loading -> Result.Loading
      }
    }
}
