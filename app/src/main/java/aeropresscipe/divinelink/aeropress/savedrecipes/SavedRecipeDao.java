package aeropresscipe.divinelink.aeropress.savedrecipes;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public abstract class SavedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertLikedRecipe(SavedRecipeDomain savedRecipeDomain);

    @Query("SELECT * FROM SavedRecipes")
    public abstract List<SavedRecipeDomain> getSavedRecipes();

    @Query("DELETE FROM SavedRecipes")
    abstract void deleteAll();

    @Query("DELETE FROM SavedRecipes WHERE diceTemperature = :diceTemperature AND" +
            " groundSize = :groundSize AND" +
            " brewTime = :brewTime AND" +
            " brewingMethod = :brewingMethod AND" +
            " bloomTime = :bloomTime AND" +
            " bloomWater = :bloomWater AND" +
            " brewWaterAmount = :brewWaterAmount AND" +
            " coffeeAmount = :coffeeAmount ")
    public abstract void deleteCurrent(int diceTemperature,
                             String groundSize,
                             int brewTime,
                             String brewingMethod,
                             int bloomTime,
                             int bloomWater,
                             int brewWaterAmount,
                             int coffeeAmount);



}