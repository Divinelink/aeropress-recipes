package aeropresscipe.divinelink.aeropress.generaterecipe

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Recipe")
data class DiceDomain(
    var recipe: Recipe,
    var isBrewing: Boolean = false
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
