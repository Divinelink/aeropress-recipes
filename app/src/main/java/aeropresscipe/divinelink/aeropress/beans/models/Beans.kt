package aeropresscipe.divinelink.aeropress.beans

import aeropresscipe.divinelink.aeropress.beans.models.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.models.RoastLevel
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
