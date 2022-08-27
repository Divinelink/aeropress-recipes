package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ISavedRecipesServices {
    suspend fun getRecipesFromDB(): List<SavedRecipeDomain>
    suspend fun deleteRecipe(recipe: Recipe): List<SavedRecipeDomain>
    suspend fun getSingleRecipe(recipe: Recipe): Recipe
}

class SavedRecipesServices @Inject constructor(
    private val recipeDao: RecipeDao,
    private val historyDao: HistoryDao,
    private val savedRecipeDao: SavedRecipeDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ISavedRecipesServices {
    override suspend fun getRecipesFromDB(): List<SavedRecipeDomain> {
        return withContext(dispatcher) {
            savedRecipeDao.savedRecipes
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe): List<SavedRecipeDomain> {
        return withContext(dispatcher) {
            val recipes = savedRecipeDao
            val history = historyDao

            recipes.delete(recipe)
            history.updateLike(recipe = recipe, false)

            return@withContext recipes.savedRecipes
        }
    }

    override suspend fun getSingleRecipe(recipe: Recipe): Recipe {
        return withContext(dispatcher) {
            val recipeDao = recipeDao
            // We also have to update the current recipe, so when we start the timer, the current recipe will be displayed.
            // Also, when we go back to the starting fragment, the displayed recipe will be the one we select from the favourites.
            Log.d("getSpecificRecipeFromDB", "Updates the current recipe to the one we selected to brew from favourites.")
            recipeDao.updateRecipe(recipe)
            return@withContext recipe
        }
    }
}
