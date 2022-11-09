package aeropresscipe.divinelink.aeropress.favorites

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val dbRemote: FavoritesServices
) : BaseRepository() {

    fun getFavorites(
        completionBlock: (List<Favorites>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getFavorites() }

    fun deleteRecipe(
        recipe: Recipe,
        completionBlock: (List<Favorites>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.deleteRecipe(recipe) }
}
