package aeropresscipe.divinelink.aeropress.beans.models

import androidx.room.Entity

@Entity(tableName = "Beans")
data class Beans(
    var name: String,
    var roasterName: String,
    var origin: String,
    var roastDate: String,
    var roastLevel: RoastLevel,
    var process: ProcessMethod,
    var rating: Int,
    var tastingNotes: String,
    var additionalNotes: String
)
