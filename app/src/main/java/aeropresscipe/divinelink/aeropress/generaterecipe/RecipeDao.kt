package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.generaterecipe.factory.RecipeBuilder
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import timber.log.Timber

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DiceDomain)

    @Query("SELECT * FROM Recipe ORDER BY id DESC LIMIT 1")
    fun singleRecipe(): DiceDomain?

    @Query("DELETE FROM Recipe")
    fun deleteAll()

    @Update
    suspend fun updateBrewing(recipe: DiceDomain)

    @Query("UPDATE Recipe SET isBrewing=:isBrewing, timeStartedMillis=:timeStartedMillis WHERE id = :id")
    fun updateBrewingState(isBrewing: Boolean, timeStartedMillis: Long, id: Int)

    @Transaction
    suspend fun updateRecipe(recipe: Recipe) {
        deleteAll()
        insertRecipe(DiceDomain(recipe))
    }

    @Transaction
    suspend fun getRecipe(): DiceDomain {
        return if (singleRecipe() == null) {
            Timber.d("Recipe is null, generating a new one.")
            val recipe = RecipeBuilder().recipe
            updateRecipe(recipe)
            DiceDomain(recipe, false)
        } else {
            Timber.d("Fetching existing recipe.")
            singleRecipe() as DiceDomain
        }
    }
}
