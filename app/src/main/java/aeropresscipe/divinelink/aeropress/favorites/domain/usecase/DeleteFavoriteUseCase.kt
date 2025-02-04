package aeropresscipe.divinelink.aeropress.favorites.domain.usecase

import aeropresscipe.divinelink.aeropress.base.di.IoDispatcher
import aeropresscipe.divinelink.aeropress.favorites.domain.repository.FavoritesRepository
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import gr.divinelink.core.util.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
  private val repository: FavoritesRepository,
  @IoDispatcher dispatcher: CoroutineDispatcher,
) : UseCase<Recipe, Unit>(dispatcher) {

  override suspend fun execute(parameters: Recipe) {
    repository.deleteFavorite(recipe = parameters)
  }
}
