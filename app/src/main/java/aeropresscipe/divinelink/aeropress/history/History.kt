package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import androidx.room.Entity
import androidx.room.PrimaryKey
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel

@Entity(tableName = "HistoryRecipes")
data class History(
    var recipe: Recipe,
    var dateBrewed: String,
    var isRecipeLiked: Boolean = false,
) : MappingModel {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    override fun areItemsTheSame(newItem: Any): Boolean {
        newItem as History
        return id == newItem.id
    }

    override fun areContentsTheSame(newItem: Any): Boolean {
        newItem as History
        return this == newItem
    }

    override fun getChangePayload(newItem: Any): Any? {
        newItem as History
        return if (isRecipeLiked == newItem.isRecipeLiked) {
            super.getChangePayload(newItem)
        } else {
            newItem.isRecipeLiked
        }
    }

}
