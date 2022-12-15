package aeropresscipe.divinelink.aeropress.favorites.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesListResult
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchAllFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<Favorites>>(dispatcher) {

    override fun execute(
        parameters: Unit,
    ): Flow<FavoritesListResult> {
        return repository.fetchAllFavorites().map { result ->
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
