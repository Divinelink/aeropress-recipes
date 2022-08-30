package aeropresscipe.divinelink.aeropress.home

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val remote: HomeRemote,
) : BaseRepository() {

    fun checkIfBrewing(
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { remote.alreadyBrewing() }

}

interface IHomeRemote {
    suspend fun alreadyBrewing(): Boolean
}

class HomeRemote @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val recipeDao: RecipeDao,

    ) : IHomeRemote {
    override suspend fun alreadyBrewing(): Boolean {
        return withContext(dispatcher) {
            recipeDao.singleRecipe.isBrewing
        }
    }
}
