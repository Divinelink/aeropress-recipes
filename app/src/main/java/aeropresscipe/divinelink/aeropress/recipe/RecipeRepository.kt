package aeropresscipe.divinelink.aeropress.recipe

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.recipe.factory.RecipeBuilder
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RecipeRepository {
  suspend fun checkIfBrewing(
    completionBlock: (Boolean) -> Unit,
  )

  fun getRecipe(
    completionBlock: (DiceDomain) -> Unit,
  )

  fun createNewRecipe(
    completionBlock: (DiceDomain) -> Unit,
  )
}

class RoomRecipeRepository @Inject constructor(
  private val remote: GenerateRecipeRemote,
) : RecipeRepository, BaseRepository() {

  override suspend fun checkIfBrewing(completionBlock: (Boolean) -> Unit) {
    performTransaction(
      completionBlock = completionBlock,
      transaction = remote::alreadyBrewing,
    )
  }

  override fun getRecipe(completionBlock: (DiceDomain) -> Unit) {
    performTransaction(
      completionBlock = completionBlock,
      transaction = remote::getRecipe,
    )
  }

  override fun createNewRecipe(completionBlock: (DiceDomain) -> Unit) {
    performTransaction(
      completionBlock = completionBlock,
      transaction = remote::createNewRecipe,
    )
  }
}

interface IGenerateRecipeRemote {
  suspend fun alreadyBrewing(): Boolean
  suspend fun createNewRecipe(): DiceDomain
  suspend fun getRecipe(): DiceDomain
}

/**
 * Open for Unit Tests
 */
open class GenerateRecipeRemote @Inject constructor(
  private val recipeDao: RecipeDao,
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : IGenerateRecipeRemote {

  override suspend fun alreadyBrewing(): Boolean {
    return withContext(dispatcher) {
      recipeDao.getRecipe().isBrewing
    }
  }

  override suspend fun createNewRecipe(): DiceDomain {
    return withContext(dispatcher) {
      val recipe = RecipeBuilder().recipe
      recipeDao.updateRecipe(recipe)
      return@withContext DiceDomain(recipe, false)
    }
  }

  override suspend fun getRecipe(): DiceDomain {
    return withContext(dispatcher) {
      recipeDao.getRecipe()
    }
  }
}
