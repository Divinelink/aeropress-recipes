package aeropresscipe.divinelink.aeropress.recipe

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.recipe.factory.RecipeBuilder
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val dbRemote: GenerateRecipeRemote,
) : BaseRepository() {

    fun checkIfBrewing(
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.alreadyBrewing() }

    fun getRecipe(
        completionBlock: (DiceDomain) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getRecipe() }

    fun createNewRecipe(
        completionBlock: (DiceDomain) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.createNewRecipe() }
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
    @IoDispatcher private val dispatcher: CoroutineDispatcher
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
