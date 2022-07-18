package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    private val remote: HistoryRemote,
) : BaseRepository() {

    fun getHistory(
        completionBlock: (List<History>) -> Unit
    ) = performTransaction(completionBlock) { remote.getHistory() }

    fun startBrew(
        recipe: Recipe,
        completionBlock: (Recipe?) -> Unit
    ) = performTransaction(completionBlock) { remote.getRecipe(recipe) }

    fun likeRecipe(
        recipe: SavedRecipeDomain,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { remote.likeRecipe(recipe) }
}

interface IHistoryRemote {
    suspend fun getHistory(): List<History>
    suspend fun getRecipe(recipe: Recipe): Recipe
    suspend fun likeRecipe(recipe: SavedRecipeDomain): Boolean
}

class HistoryRemote @Inject constructor(
    private val recipeDao: RecipeDao,
    private val historyDao: HistoryDao,
    private val savedRecipeDao: SavedRecipeDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : IHistoryRemote {

    override suspend fun getHistory(): List<History> {
        return withContext(dispatcher) {
            historyDao.historyRecipes ?: emptyList()
        }
    }

    override suspend fun getRecipe(recipe: Recipe): Recipe {
        return withContext(dispatcher) {
            recipeDao.updateRecipe(recipe)
            recipe
        }
    }

    override suspend fun likeRecipe(recipe: SavedRecipeDomain): Boolean {
        return withContext(dispatcher) {
            if (savedRecipeDao.recipeExists(recipe.recipe)) {
                savedRecipeDao.delete(recipe.recipe)
                historyDao.updateLike(recipe.recipe, false)
                false
            } else {
                savedRecipeDao.insertLikedRecipe(recipe)
                historyDao.updateLike(recipe.recipe, true)
                true
            }
        }
    }
}
