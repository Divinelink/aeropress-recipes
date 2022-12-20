package aeropresscipe.divinelink.aeropress.beans.domain.model

data class Bean(
    val id: String,
    val name: String,
    val roasterName: String,
    val origin: String,
    val roastDate: String,
    val roastLevel: RoastLevel,
    val process: ProcessMethod,
    val rating: Int,
    val tastingNotes: String,
    val additionalNotes: String,
)
