package aeropresscipe.divinelink.aeropress.favorites.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesListResult
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesRepository
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Recipe, List<Favorites>>(dispatcher) {

    override fun execute(
        parameters: Recipe,
    ): Flow<FavoritesListResult> {
        return repository.deleteFavorite(parameters).map { result ->
            when (result) {
                is Result.Success -> {
                    result
                }
                is Result.Error -> {
                    Result.Error(result.exception)
                }
                Result.Loading -> Result.Loading
            }
        }
    }
}
