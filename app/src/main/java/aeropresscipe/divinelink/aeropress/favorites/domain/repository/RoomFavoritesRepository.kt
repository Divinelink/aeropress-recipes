package aeropresscipe.divinelink.aeropress.favorites.domain.repository

import aeropresscipe.divinelink.aeropress.favorites.FavoritesDao
import aeropresscipe.divinelink.aeropress.history.HistoryDao
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesRepository @Inject constructor(
    private val historyDao: HistoryDao,
    private val favoritesDao: FavoritesDao,
) : FavoritesRepository {

    override fun fetchAllFavorites(): Flow<FavoritesListResult> {
        return favoritesDao
            .fetchAllFavorites()
            .map { favorites ->
                Result.Success(favorites)
            }
    }

    override suspend fun deleteFavorite(recipe: Recipe): Result<Unit> {
        favoritesDao.delete(recipe)
        historyDao.updateLike(recipe, false)

        return Result.Success(Unit)
    }
}
