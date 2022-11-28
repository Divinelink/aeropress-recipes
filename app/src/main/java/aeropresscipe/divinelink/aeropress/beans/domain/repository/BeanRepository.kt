package aeropresscipe.divinelink.aeropress.beans.domain.repository

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import gr.divinelink.core.util.domain.Result

typealias BeanListResult = Result<List<Bean>>

/**
 * The data layer for any requests related to Beans Tracker.
 */
interface BeanRepository {

    suspend fun fetchAllBeans(): BeanListResult

    /**
     * Add a new [bean] to the user's stored beans.
     */
    suspend fun addBean(
        bean: Bean,
    ): Result<Unit>

    suspend fun fetchBean(
        bean: Bean,
    ): Result<Bean>

    /**
     * Update existing [bean] from user's input.
     */
    suspend fun updateBean(
        bean: Bean,
    ): Result<Unit>
}
