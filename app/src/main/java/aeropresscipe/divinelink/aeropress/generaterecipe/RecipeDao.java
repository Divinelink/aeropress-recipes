package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.List;

import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertRecipe(DiceDomain diceDomain);

    @Query("SELECT * FROM Recipe ORDER BY id DESC LIMIT  1")
    public abstract DiceDomain getSingleRecipe();

    @Query("SELECT * FROM Recipe")
    public abstract List<DiceDomain> getRecipeHistory();


    @Query("DELETE FROM Recipe")
    abstract void deleteAll();

    @Transaction
    public void updateRecipe(DiceDomain diceDomain) {
        //TODO make it remove items if size > 20 for example.
//        deleteAll();
        insertRecipe(diceDomain);
    }

}
