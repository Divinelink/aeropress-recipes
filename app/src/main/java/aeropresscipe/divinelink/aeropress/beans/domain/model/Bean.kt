package aeropresscipe.divinelink.aeropress.beans.domain.model

import java.io.Serializable

data class Bean(
    val id: String,
    val name: String,
    val roasterName: String,
    val origin: String,
    val roastDate: String,
    val roastLevel: RoastLevel?,
    val process: ProcessMethod?,
    val rating: Int,
    val tastingNotes: String,
    val additionalNotes: String,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
