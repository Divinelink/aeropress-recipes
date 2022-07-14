package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenerateRecipeRepository @Inject constructor(
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
            recipeDao.singleRecipe?.isBrewing == true
        }
    }

    override suspend fun createNewRecipe(): DiceDomain {
        return withContext(dispatcher) {
            val recipe = GenerateRecipe().finalRecipe
            recipeDao.updateRecipe(recipe)
            DiceDomain(recipe, false)
        }
    }

    override suspend fun getRecipe(): DiceDomain {
        return withContext(dispatcher) {
            val recipe = recipeDao.singleRecipe
            if (recipe == null) {
                // Create a new recipe if there's no recipe available
                val newRecipe = GenerateRecipe().finalRecipe
                recipeDao.updateRecipe(newRecipe)
                return@withContext DiceDomain(newRecipe, false)
            } else {
                recipe
            }
        }
    }
}
