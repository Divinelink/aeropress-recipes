package aeropresscipe.divinelink.aeropress.favorites.ui

import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe

/**
 * All of the necessary configurations for the favorites list screen UI.
 * @param brewRecipe starts a new with the given recipe.
 */
data class FavoritesViewState(
    val isLoading: Boolean = true,
    val emptyRecipes: Boolean? = null,
    val recipes: List<Favorites>? = null,
    val errorMessage: String? = null,
    val brewRecipe: Recipe? = null,
)
