package aeropresscipe.divinelink.aeropress.fakes.dao

import aeropresscipe.divinelink.aeropress.base.data.local.bean.BeanDAO
import aeropresscipe.divinelink.aeropress.base.data.local.bean.PersistableBean
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeBeanDAO {

    val mock: BeanDAO = mock()

    fun mockFetchAllBeans(
        result: Flow<List<PersistableBean>>,
    ) {
        whenever(
            mock.fetchAllBeans()
        ).thenReturn(
            result
        )
    }

    fun mockFetchBeanById(
        id: String,
        result: PersistableBean,
    ) {
        whenever(mock.fetchBeanById(id)).thenReturn(result)
    }

    suspend fun verifyInsertBean(
        bean: PersistableBean,
    ) {
        verify(mock).insertBean(bean)
    }

    suspend fun verifyUpdateBean(bean: PersistableBean) {
        verify(mock).updateBean(bean)
    }

    fun verifyRemoveBean(id: String) {
        verify(mock).removeBean(id)
    }
}
