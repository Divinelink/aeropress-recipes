package aeropresscipe.divinelink.aeropress.customviews

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import android.view.View

sealed class RecipeCard(
    open val recipe: Recipe,
    open val brewDate: String? = null,
    val brewDateVisibility: Int,
    val likeButtonVisibility: Int
) {
    data class FinishCard(override val recipe: Recipe) : RecipeCard(
        recipe = recipe,
        brewDateVisibility = View.GONE,
        likeButtonVisibility = View.GONE
    )

    data class FavoritesCard(override val recipe: Recipe, override val brewDate: String?) : RecipeCard(
        recipe = recipe,
        brewDate = brewDate,
        brewDateVisibility = View.VISIBLE,
        likeButtonVisibility = View.GONE
    )

    data class HistoryCard(override val recipe: Recipe, override val brewDate: String?) : RecipeCard(
        recipe = recipe,
        brewDate = brewDate,
        brewDateVisibility = View.VISIBLE,
        likeButtonVisibility = View.VISIBLE
    )
}
