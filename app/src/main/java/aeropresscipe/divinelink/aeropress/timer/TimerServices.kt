package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.FavoritesDao
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import aeropresscipe.divinelink.aeropress.recipe.RecipeDao
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import com.divinelink.aeropress.recipes.history.History
import gr.divinelink.core.util.utils.DateUtil.getCurrentDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ITimerServices {
  suspend fun likeRecipe(recipe: Recipe): Boolean
  suspend fun addToHistory(recipe: Recipe)
  suspend fun isRecipeSaved(recipe: Recipe?): Boolean
  suspend fun updateBrewingState(brewing: Boolean, timeStartedMillis: Long)
  suspend fun getResumeTimes(): DiceDomain
}

open class TimerServices @Inject constructor(
  private val recipeDao: RecipeDao,
  private val historyDao: HistoryDao,
  private val savedRecipeDao: FavoritesDao,
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ITimerServices {

  override suspend fun likeRecipe(recipe: Recipe): Boolean {
    return withContext(dispatcher) {
      if (savedRecipeDao.recipeExists(recipe)) {
        savedRecipeDao.delete(recipe)
        historyDao.updateLike(recipe, false)
        false
      } else {
        savedRecipeDao.insertLikedRecipe(Favorites(recipe, getCurrentDate()))
        historyDao.updateLike(recipe, true)
        true
      }
    }
  }

  override suspend fun addToHistory(
    recipe: Recipe,
  ) {
    withContext(dispatcher) {
      val isLiked = savedRecipeDao.recipeExists(recipe)
      val historyRecipe = historyDao.getHistoryRecipe(recipe)
      historyRecipe?.apply {
        dateBrewed = getCurrentDate()
        isRecipeLiked = isLiked
      }
      historyDao.updateRecipe(historyRecipe ?: History(recipe, getCurrentDate(), isLiked))
    }
  }

  override suspend fun isRecipeSaved(
    recipe: Recipe?,
  ): Boolean {
    return withContext(dispatcher) {
      savedRecipeDao.recipeExists(recipe)
    }
  }

  override suspend fun updateBrewingState(
    brewing: Boolean,
    timeStartedMillis: Long,
  ) {
    withContext(dispatcher) {
      recipeDao.updateBrewingState(brewing, timeStartedMillis, recipeDao.getRecipe().id)
    }
  }

  override suspend fun getResumeTimes(): DiceDomain {
    return withContext(dispatcher) {
      recipeDao.getRecipe()
    }
  }
}
