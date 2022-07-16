package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedRecipes")
data class SavedRecipeDomain(
    var recipe: Recipe,
    var dateBrewed: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
