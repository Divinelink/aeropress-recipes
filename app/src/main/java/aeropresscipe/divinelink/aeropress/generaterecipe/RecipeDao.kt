package aeropresscipe.divinelink.aeropress.generaterecipe

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRecipe(recipe: DiceDomain)

    @get:Query("SELECT * FROM Recipe ORDER BY id DESC LIMIT 1")
    abstract val singleRecipe: DiceDomain

    @Query("DELETE FROM Recipe")
    abstract fun deleteAll()

    @Transaction
    open fun updateRecipe(recipe: Recipe) {
        deleteAll()
        insertRecipe(DiceDomain(recipe, false))
    }
}