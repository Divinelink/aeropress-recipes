package aeropresscipe.divinelink.aeropress.favorites.domain.repository

import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow

typealias FavoritesListResult = Result<List<Favorites>>

interface FavoritesRepository {

    fun fetchAllFavorites(): Flow<FavoritesListResult>

    suspend fun deleteFavorite(recipe: Recipe): Result<Unit>
}
