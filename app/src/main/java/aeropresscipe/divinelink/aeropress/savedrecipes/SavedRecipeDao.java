package aeropresscipe.divinelink.aeropress.savedrecipes;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe;


@Dao
public abstract class SavedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertLikedRecipe(SavedRecipeDomain savedRecipeDomain);

    @Query("SELECT * FROM SavedRecipes")
    public abstract List<SavedRecipeDomain> getSavedRecipes();

    @Query("SELECT EXISTS (SELECT 1 FROM SavedRecipes WHERE  recipe= :recipe)")
    public abstract boolean recipeExists(Recipe recipe);

    @Query("DELETE FROM SavedRecipes")
    abstract void deleteAll();

    @Query("DELETE FROM SavedRecipes WHERE recipe=:recipe")
    public abstract void delete(Recipe recipe);

}
