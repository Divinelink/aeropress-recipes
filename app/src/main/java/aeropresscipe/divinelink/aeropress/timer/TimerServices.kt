package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.HomeDatabase
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface ITimerServices {
    suspend fun likeCurrentRecipe(recipe: SavedRecipeDomain, context: Context): Boolean
    suspend fun removeCurrentRecipe(recipe: SavedRecipeDomain, context: Context)
    suspend fun updateHistory(recipe: Recipe, brewDate: String, isLiked: Boolean, context: Context)
    suspend fun addToHistory(recipe: Recipe, brewDate: String, context: Context)
    suspend fun isRecipeSaved(recipe: Recipe?, context: Context): Boolean
}

class TimerServices(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ITimerServices {

    override suspend fun likeCurrentRecipe(recipe: SavedRecipeDomain, context: Context): Boolean {
        val database = HomeDatabase.getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val recipesDao = db.savedRecipeDao()
                if (recipesDao.recipeExists(recipe.recipe)) {
                    recipesDao.delete(recipe.recipe)
                    return@withContext false
                } else {
                    recipesDao.insertLikedRecipe(recipe)
                    return@withContext true
                }
            }
        }
    }

    override suspend fun removeCurrentRecipe(recipe: SavedRecipeDomain, context: Context) {
        val database = HomeDatabase.getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val recipesDao = db.savedRecipeDao()
                recipesDao.delete(recipe.recipe)
            }
        }
    }

    override suspend fun updateHistory(
        recipe: Recipe,
        brewDate: String,
        isLiked: Boolean,
        context: Context
    ) {
        val database = HomeDatabase.getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val historyDao = db.historyDao()
                historyDao.updateRecipe(History(recipe = recipe, dateBrewed =  brewDate, isRecipeLiked = isLiked))
            }
        }
    }

    override suspend fun addToHistory(
        recipe: Recipe,
        brewDate: String,
        context: Context
    ) {
        val database = HomeDatabase.getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val historyDao = db.historyDao()
                val isLiked = db.savedRecipeDao().recipeExists(recipe)
                historyDao.updateRecipe(History(recipe, brewDate, isLiked))
            }
        }
    }

    override suspend fun isRecipeSaved(
        recipe: Recipe?,
        context: Context
    ): Boolean {
        val database = HomeDatabase.getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                return@withContext db.savedRecipeDao().recipeExists(recipe)
            }
        }
    }
}
