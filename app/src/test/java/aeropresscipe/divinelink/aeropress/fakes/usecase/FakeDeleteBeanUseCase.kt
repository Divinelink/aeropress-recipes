package aeropresscipe.divinelink.aeropress.fakes.usecase

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.DeleteBeanUseCase
import aeropresscipe.divinelink.aeropress.beans.domain.model.AddBeanResult
import gr.divinelink.core.util.domain.Result
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDeleteBeanUseCase {

    val mock: DeleteBeanUseCase = mock()

    suspend fun mockResultDeleteBean(
        result: Result<AddBeanResult>,
    ) {
        whenever(mock(any())).thenReturn(result)
    }
}
