package com.divinelink.aeropress.recipes.history

import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.util.mapping.MappingModel
import androidx.room.Entity
import androidx.room.PrimaryKey

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
