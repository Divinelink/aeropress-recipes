package aeropresscipe.divinelink.aeropress.favorites

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IFavoritesServices {
    suspend fun getFavorites(): List<Favorites>
    suspend fun deleteRecipe(recipe: Recipe): List<Favorites>
}

class FavoritesServices @Inject constructor(
    private val historyDao: HistoryDao,
    private val favoritesDao: FavoritesDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : IFavoritesServices {
    override suspend fun getFavorites(): List<Favorites> {
        return withContext(dispatcher) {
            favoritesDao.favorites
        }
    }

    override suspend fun deleteRecipe(recipe: Recipe): List<Favorites> {
        return withContext(dispatcher) {
            val recipes = favoritesDao
            val history = historyDao

            recipes.delete(recipe)
            history.updateLike(recipe = recipe, false)

            return@withContext recipes.favorites
        }
    }
}
