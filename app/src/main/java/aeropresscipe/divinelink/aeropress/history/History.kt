package aeropresscipe.divinelink.aeropress.history

import androidx.room.PrimaryKey
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
import androidx.room.Entity

@Entity(tableName = "HistoryRecipes")
data class History(
    var recipe: Recipe,
    var dateBrewed: String,
    var isRecipeLiked: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
