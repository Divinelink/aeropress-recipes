package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import javax.inject.Inject

class SavedRecipesRepository @Inject constructor(
    private val dbRemote: SavedRecipesServices
) : BaseRepository() {

    fun getFavorites(
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getFavorites() }

    fun deleteRecipe(
        recipe: Recipe,
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.deleteRecipe(recipe) }

    fun startBrew(
        recipe: Recipe,
        completionBlock: (Recipe?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getSingleRecipe(recipe) }
}
