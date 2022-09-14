package aeropresscipe.divinelink.aeropress.favorites

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLikedRecipe(savedRecipeDomain: Favorites)

    @get:Query("SELECT * FROM SavedRecipes")
    val favorites: List<Favorites>

    @Query("SELECT EXISTS (SELECT 1 FROM SavedRecipes WHERE  recipe= :recipe)")
    fun recipeExists(recipe: Recipe?): Boolean

    @Query("DELETE FROM SavedRecipes")
    fun deleteAll()

    @Query("DELETE FROM SavedRecipes WHERE recipe=:recipe")
    fun delete(recipe: Recipe?)
}