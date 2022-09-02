package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ISavedRecipesServices {
    suspend fun getFavorites(): List<SavedRecipeDomain>
    suspend fun deleteRecipe(recipe: Recipe): List<SavedRecipeDomain>
}

class SavedRecipesServices @Inject constructor(
    private val historyDao: HistoryDao,
    private val savedRecipeDao: SavedRecipeDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ISavedRecipesServices {
    override suspend fun getFavorites(): List<SavedRecipeDomain> {
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
}
