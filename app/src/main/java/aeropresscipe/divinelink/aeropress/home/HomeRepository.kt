package aeropresscipe.divinelink.aeropress.home

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.recipe.RecipeDao
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val remote: HomeRemote,
) : BaseRepository() {

    fun getRecipe(
        completionBlock: (DiceDomain) -> Unit
    ) = performTransaction(completionBlock) { remote.getRecipe() }

    fun updateRecipe(
        recipe: Recipe,
        update: Boolean,
        completionBlock: (DiceDomain) -> Unit
    ) = performTransaction(completionBlock) { remote.updateRecipe(recipe, update) }

}

interface IHomeRemote {
    suspend fun getRecipe(): DiceDomain
    suspend fun updateRecipe(recipe: Recipe, update: Boolean): DiceDomain
}

class HomeRemote @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val recipeDao: RecipeDao
) : IHomeRemote {

    override suspend fun getRecipe(): DiceDomain {
        return withContext(dispatcher) {
            return@withContext recipeDao.getRecipe()
        }
    }

    override suspend fun updateRecipe(recipe: Recipe, update: Boolean): DiceDomain {
        return withContext(dispatcher) {
            if (update) {
                recipeDao.updateRecipe(recipe)
            }
            val updatedRecipe = recipeDao.getRecipe()
            Timber.d("Recipe updated with id ${updatedRecipe.id}")
            return@withContext updatedRecipe
        }
    }
}
