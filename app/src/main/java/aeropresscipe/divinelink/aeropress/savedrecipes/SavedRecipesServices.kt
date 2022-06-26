package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.HomeDatabase.Companion.getDatabase
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
import aeropresscipe.divinelink.aeropress.history.History
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ISavedRecipesServices {
    suspend fun getRecipesFromDB(context: Context): List<SavedRecipeDomain>

    suspend fun deleteRecipe(recipe: SavedRecipeDomain, context: Context): List<SavedRecipeDomain>

    suspend fun getSingleRecipe(recipe: Recipe, context: Context): Recipe
}

class SavedRecipesServicesImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISavedRecipesServices {
    override suspend fun getRecipesFromDB(context: Context): List<SavedRecipeDomain> {
        val database = getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val recipesDao = db.savedRecipeDao()
                return@withContext recipesDao.savedRecipes
            }
        }
    }

    override suspend fun deleteRecipe(recipe: SavedRecipeDomain, context: Context): List<SavedRecipeDomain> {
        val database = getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val recipes = db.savedRecipeDao()
                val history = db.historyDao()

                recipes.delete(recipe.recipe)
                if (history.historyRecipeExists(recipe.id)) {
                    history.updateRecipe(History(
                        recipe = recipe.recipe,
                        dateBrewed = recipe.dateBrewed,
                        isRecipeLiked = false)
                    )
                }
                return@withContext recipes.savedRecipes
            }
        }
    }

    override suspend fun getSingleRecipe(recipe: Recipe, context: Context): Recipe {
        val database = getDatabase(context)
        return database.let { db ->
            withContext(dispatcher) {
                val recipeDao = db.recipeDao()
                // We also have to update the current recipe, so when we start the timer, the current recipe will be displayed.
                // Also, when we go back to the starting fragment, the displayed recipe will be the one we select from the favourites.
                Log.d("getSpecificRecipeFromDB", "Updates the current recipe to the one we selected to brew from favourites.")
                recipeDao.updateRecipe(recipe)
                return@withContext recipe
            }
        }
    }
}
