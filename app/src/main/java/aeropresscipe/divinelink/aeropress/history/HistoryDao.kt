package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.divinelink.aeropress.recipes.history.History

@Dao
interface HistoryDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertRecipeToHistory(historyDomain: History)

  @get:Query("SELECT * FROM HistoryRecipes")
  val historyRecipes: List<History>?

  @Query("SELECT * FROM HistoryRecipes WHERE recipe = :recipe")
  fun getHistoryRecipe(recipe: Recipe): History?

  @Query("SELECT Recipe FROM HistoryRecipes WHERE recipe = :recipe")
  fun getSpecificRecipe(recipe: Recipe?): Recipe?

  @Query("DELETE FROM HistoryRecipes")
  fun deleteAll()

  @Query("SELECT EXISTS (SELECT 1 FROM HistoryRecipes WHERE  id= :id)")
  fun historyRecipeExists(id: Int): Boolean

  @Query("DELETE FROM HistoryRecipes where id NOT IN (SELECT id from HistoryRecipes ORDER BY id DESC LIMIT 20)")
  fun deleteSurplus()

  @Query("DELETE FROM HistoryRecipes WHERE recipe=:recipe")
  fun delete(recipe: Recipe?)

  @Update(entity = History::class)
  fun updateHistory(recipe: History)

  @Query("UPDATE HistoryRecipes SET isRecipeLiked=:isLiked WHERE Recipe=:recipe")
  fun updateLike(recipe: Recipe, isLiked: Boolean)

  @Transaction
  fun updateRecipe(history: History) {
    delete(history.recipe)
    insertRecipeToHistory(history)
  }
}
