package aeropresscipe.divinelink.aeropress.fakes

import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesRepository
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * A fake implementation of a [FavoritesRepository] that wraps all our mock work.
 */
class FakeFavoritesRepository {

    val mock: FavoritesRepository = mock()

    fun mockFetchAllFavoritesResult(
        response: Result<List<Favorites>>,
    ) {
        whenever(
            mock.fetchAllFavorites()
        ).thenReturn(
            flowOf(response)
        )
    }

    fun mockDeleteFavorite(
        recipe: Recipe,
        response: Result<List<Favorites>>,
    ) {
        whenever(
            mock.deleteFavorite(recipe)
        ).thenReturn(
            flowOf(response)
        )
    }
}
