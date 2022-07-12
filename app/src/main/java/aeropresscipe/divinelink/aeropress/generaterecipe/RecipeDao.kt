package aeropresscipe.divinelink.aeropress.generaterecipe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DiceDomain)

    @get:Query("SELECT * FROM Recipe ORDER BY id DESC LIMIT 1")
    val singleRecipe: DiceDomain?

    @Query("DELETE FROM Recipe")
    fun deleteAll()

    @Update
    suspend fun updateBrewing(recipe: DiceDomain)

    @Query("UPDATE Recipe SET isBrewing=:isBrewing WHERE id = :id")
    fun updateBrewingState(isBrewing: Boolean, id: Int)

    @Transaction
    fun updateRecipe(recipe: Recipe) {
        deleteAll()
        insertRecipe(DiceDomain(recipe, false))
    }
}