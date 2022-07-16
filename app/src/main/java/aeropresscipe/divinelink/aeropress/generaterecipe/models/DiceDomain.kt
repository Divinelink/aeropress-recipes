package aeropresscipe.divinelink.aeropress.generaterecipe.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Recipe")
data class DiceDomain(
    var recipe: Recipe,
    var isBrewing: Boolean = false
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
