package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import gr.divinelink.core.util.utils.DateUtil.getCurrentDate
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
        favorite: SavedRecipeDomain,
        completionBlock: (History) -> Unit
    ) = performTransaction(completionBlock) { remote.likeRecipe(favorite) }

    fun clearHistory(
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { remote.clearHistory() }
}

interface IHistoryRemote {
    suspend fun getHistory(): List<History>
    suspend fun getRecipe(recipe: Recipe): Recipe
    suspend fun likeRecipe(favorite: SavedRecipeDomain): History
    suspend fun clearHistory()
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

    override suspend fun likeRecipe(favorite: SavedRecipeDomain): History {
        val recipe = favorite.recipe
        return withContext(dispatcher) {
            if (savedRecipeDao.recipeExists(recipe)) {
                savedRecipeDao.delete(recipe)
                historyDao.updateLike(recipe, false)
            } else {
                savedRecipeDao.insertLikedRecipe(favorite)
                historyDao.updateLike(recipe, true)
            }
            historyDao.getHistoryRecipe(recipe) ?: History(recipe, favorite.dateBrewed, true)
        }
    }

    override suspend fun clearHistory() {
        return withContext(dispatcher) {
            historyDao.deleteAll()
        }
    }
}
