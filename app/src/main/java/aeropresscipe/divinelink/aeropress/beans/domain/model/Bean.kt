package aeropresscipe.divinelink.aeropress.beans.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bean")
data class Bean(
    @PrimaryKey
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
