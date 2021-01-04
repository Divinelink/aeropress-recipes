package aeropresscipe.divinelink.aeropress.generaterecipe;

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


    @Query("DELETE FROM Recipe")
    abstract void deleteAll();

    @Transaction
    public void updateRecipe(DiceDomain diceDomain) {
        deleteAll();
        insertRecipe(diceDomain);
    }

}
