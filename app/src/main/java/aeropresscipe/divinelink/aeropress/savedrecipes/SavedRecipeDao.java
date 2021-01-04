package aeropresscipe.divinelink.aeropress.savedrecipes;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public abstract class SavedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertLikedRecipe(SavedRecipeDomain savedRecipeDomain);

    @Query("SELECT * FROM SavedRecipes")
    public abstract List<SavedRecipeDomain> getSavedRecipes();

    @Query("SELECT EXISTS (SELECT 1 FROM SavedRecipes WHERE  id= :id)")
    public abstract boolean recipeExists(int id);

    @Query("DELETE FROM SavedRecipes")
    abstract void deleteAll();

    @Delete
    public abstract void delete(SavedRecipeDomain recipeDomain);


//    @Query("SELECT * FROM SavedRecipes WHERE RowID=:position")
//    public abstract SavedRecipeDomain getSingleRecipe(int position);




}