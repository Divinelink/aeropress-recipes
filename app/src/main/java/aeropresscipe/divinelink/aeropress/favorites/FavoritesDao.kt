package aeropresscipe.divinelink.aeropress.favorites

import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertLikedRecipe(savedRecipeDomain: Favorites)

  @Query("SELECT * FROM SavedRecipes")
  fun fetchAllFavorites(): Flow<List<Favorites>>

  @Query("SELECT EXISTS (SELECT 1 FROM SavedRecipes WHERE  recipe= :recipe)")
  fun recipeExists(recipe: Recipe?): Boolean

  @Query("DELETE FROM SavedRecipes")
  fun deleteAll()

  @Query("DELETE FROM SavedRecipes WHERE recipe=:recipe")
  fun delete(recipe: Recipe?)
}
