package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SavedRecipes")
data class SavedRecipeDomain(
    var recipe: Recipe,
    var dateBrewed: String
) : MappingModel {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun areItemsTheSame(newItem: Any): Boolean {
        newItem as SavedRecipeDomain
        return id == newItem.id
    }

    override fun areContentsTheSame(newItem: Any): Boolean {
        return hashCode() == newItem.hashCode()
    }

}
