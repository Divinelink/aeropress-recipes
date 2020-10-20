package aeropresscipe.divinelink.aeropress.timer;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public abstract class LikedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertLikedRecipe(LikedRecipeDomain likedRecipeDomain);

    @Query("SELECT * FROM LikedRecipes")
    public abstract LikedRecipeDomain getLikedRecipes();



}