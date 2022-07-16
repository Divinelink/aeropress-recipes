package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HistoryRecipes")
data class History(
    var recipe: Recipe,
    var dateBrewed: String,
    var isRecipeLiked: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
