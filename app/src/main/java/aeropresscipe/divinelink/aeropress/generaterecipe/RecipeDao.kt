package aeropresscipe.divinelink.aeropress.generaterecipe

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DiceDomain)

    @get:Query("SELECT * FROM Recipe ORDER BY id DESC LIMIT 1")
    val singleRecipe: DiceDomain

    @Query("DELETE FROM Recipe")
    fun deleteAll()

    @Transaction
    fun updateRecipe(recipe: Recipe) {
        deleteAll()
        insertRecipe(DiceDomain(recipe, false))
    }
}