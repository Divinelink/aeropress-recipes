package aeropresscipe.divinelink.aeropress.fakes

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.repository.BeanRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * A fake implementation of a [BeanRepository] that wraps all our mock work.
 */
class FakeBeanRepository {

    val mock: BeanRepository = mock()

    suspend fun givenAddBeanResult(
        bean: Bean,
        beanResult: Result<Unit>,
    ) {
        whenever(mock.addBean(bean)).thenReturn(beanResult)
    }

    fun mockFetchAllBeansResult(
        response: Result<List<Bean>>,
    ) {
        whenever(
            mock.fetchAllBeans()
        ).thenReturn(
            flowOf(response)
        )
    }
}
