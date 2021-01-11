package aeropresscipe.divinelink.aeropress.history;

import java.util.List;

import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipeToHistory(HistoryDomain historyDomain);

    @Query("SELECT * FROM HistoryRecipes")
    public abstract List<HistoryDomain> getHistoryRecipes();

    @Query("DELETE FROM HistoryRecipes")
    abstract void deleteAll();

    @Query("DELETE FROM HistoryRecipes where id NOT IN (SELECT id from HistoryRecipes ORDER BY id DESC LIMIT 10)")
    abstract void deleteSurplus();


}