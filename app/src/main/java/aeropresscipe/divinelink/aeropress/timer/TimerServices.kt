package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ITimerServices {
    suspend fun likeCurrentRecipe(recipe: SavedRecipeDomain): Boolean
    suspend fun removeCurrentRecipe(recipe: SavedRecipeDomain)
    suspend fun updateHistory(recipe: Recipe, brewDate: String, isLiked: Boolean)
    suspend fun addToHistory(recipe: Recipe, brewDate: String)
    suspend fun isRecipeSaved(recipe: Recipe?): Boolean
    suspend fun updateBrewingState(brewing: Boolean)
    suspend fun updateTimes(bloomEndTimeMillis: Long, brewEndTimeMillis: Long)
    suspend fun getResumeTimes(): DiceDomain
}

open class TimerServices @Inject constructor(
    private val recipeDao: RecipeDao,
    private val historyDao: HistoryDao,
    private val savedRecipeDao: SavedRecipeDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ITimerServices {

    override suspend fun likeCurrentRecipe(recipe: SavedRecipeDomain): Boolean {
        return withContext(dispatcher) {
            if (savedRecipeDao.recipeExists(recipe.recipe)) {
                savedRecipeDao.delete(recipe.recipe)
                false
            } else {
                savedRecipeDao.insertLikedRecipe(recipe)
                true
            }
        }
    }

    override suspend fun removeCurrentRecipe(recipe: SavedRecipeDomain) {
        withContext(dispatcher) {
            savedRecipeDao.delete(recipe.recipe)
        }
    }

    override suspend fun updateHistory(
        recipe: Recipe,
        brewDate: String,
        isLiked: Boolean
    ) {
        withContext(dispatcher) {
            historyDao.updateRecipe(History(recipe = recipe, dateBrewed = brewDate, isRecipeLiked = isLiked))
        }
    }

    override suspend fun addToHistory(
        recipe: Recipe,
        brewDate: String
    ) {
        withContext(dispatcher) {
            val isLiked = savedRecipeDao.recipeExists(recipe)
            historyDao.updateRecipe(History(recipe, brewDate, isLiked))
        }
    }

    override suspend fun isRecipeSaved(
        recipe: Recipe?
    ): Boolean {
        return withContext(dispatcher) {
            savedRecipeDao.recipeExists(recipe)
        }
    }

    override suspend fun updateBrewingState(
        brewing: Boolean
    ) {
        withContext(dispatcher) {
            recipeDao.updateBrewingState(brewing, recipeDao.singleRecipe.id)
        }
    }

    override suspend fun updateTimes(bloomEndTimeMillis: Long, brewEndTimeMillis: Long) {
        return withContext(dispatcher) {
            recipeDao.updateTimes(bloomEndTimeMillis, brewEndTimeMillis, recipeDao.singleRecipe.id)
        }
    }

    override suspend fun getResumeTimes(): DiceDomain {
        return withContext(dispatcher) {
            recipeDao.singleRecipe
        }
    }
}
