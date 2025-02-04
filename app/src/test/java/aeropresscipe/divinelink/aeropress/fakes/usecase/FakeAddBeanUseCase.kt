package aeropresscipe.divinelink.aeropress.fakes.usecase

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.AddBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import gr.divinelink.core.util.domain.Result
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeAddBeanUseCase {

  val mock: AddBeanUseCase = mock()

  suspend fun mockAddBeanResult(
    result: Result<AddBeanResult>,
  ) {
    whenever(mock.invoke(any())).thenReturn(result)
  }
}

/* class FakeAddBeanUseCase @Inject constructor(
    beanRepository: BeanRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : AddBeanUseCase(
    beanRepository = beanRepository,
    dispatcher = dispatcher,
) {
    private val addBeanResults: MutableMap<Bean, Result<AddBeanResult>> = mutableMapOf()

    fun mockResultForAddBean(
        bean: Bean,
        result: Result<AddBeanResult>,
    ) {
        addBeanResults[bean] = result
    }

    override suspend fun invoke(parameters: Bean): Result<AddBeanResult> {
        return addBeanResults[parameters]!!
    }
}
*/
