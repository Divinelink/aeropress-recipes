package aeropresscipe.divinelink.aeropress.fakes

import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesListResult
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesRepository
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * A fake implementation of a [FavoritesRepository] that wraps all our mock work,
 * but also allows our called to mock and verify calls to this repo.
 */
class FakeFavoritesRepository : FavoritesRepository {

  val mock: FavoritesRepository = mock()

  val deleteFavoritesResult: MutableMap<Recipe, Result<Unit>> = mutableMapOf()

  fun mockFetchAllFavoritesResult(response: Result<List<Favorites>>) {
    whenever(
      mock.fetchAllFavorites(),
    ).thenReturn(
      flowOf(response),
    )
  }

  override fun fetchAllFavorites(): Flow<FavoritesListResult> {
    TODO("Not yet implemented")
  }

  override suspend fun deleteFavorite(recipe: Recipe): Result<Unit> =
    deleteFavoritesResult[recipe]!!
}
