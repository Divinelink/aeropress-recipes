package aeropresscipe.divinelink.aeropress.fakes.usecase

import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import aeropresscipe.divinelink.aeropress.beans.domain.usecase.UpdateBeanUseCase
import gr.divinelink.core.util.domain.Result
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeUpdateBeanUseCase {

  val mock: UpdateBeanUseCase = mock()

  suspend fun mockResultUpdateBean(
    result: Result<AddBeanResult>,
  ) {
    whenever(mock(any())).thenReturn(result)
  }
}
