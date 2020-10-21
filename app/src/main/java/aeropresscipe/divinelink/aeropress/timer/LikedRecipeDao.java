package aeropresscipe.divinelink.aeropress.timer;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverter;


@Dao
public abstract class LikedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertLikedRecipe(LikedRecipeDomain likedRecipeDomain);

    @Query("SELECT * FROM LikedRecipes")
    public abstract List<LikedRecipeDomain> getLikedRecipes();

    @Query("DELETE FROM LikedRecipes")
    abstract void deleteAll();

    @Query("DELETE FROM LikedRecipes WHERE diceTemperature = :diceTemperature AND" +
            " groundSize = :groundSize AND" +
            " brewTime = :brewTime AND" +
            " brewingMethod = :brewingMethod AND" +
            " bloomTime = :bloomTime AND" +
            " bloomWater = :bloomWater AND" +
            " brewWaterAmount = :brewWaterAmount AND" +
            " coffeeAmount = :coffeeAmount ")
    abstract void deleteCurrent(int diceTemperature,
                             String groundSize,
                             int brewTime,
                             String brewingMethod,
                             int bloomTime,
                             int bloomWater,
                             int brewWaterAmount,
                             int coffeeAmount);



}