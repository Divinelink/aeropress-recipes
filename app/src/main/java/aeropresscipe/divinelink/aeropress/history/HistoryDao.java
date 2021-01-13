package aeropresscipe.divinelink.aeropress.history;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

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

    @Query("DELETE FROM HistoryRecipes WHERE id=:id")
    abstract void delete(int id);

    @Transaction
    public void updateRecipe(HistoryDomain historyDomain) {
        delete(historyDomain.getId());
        insertRecipeToHistory(historyDomain);
    }
}