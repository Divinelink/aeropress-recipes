package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import javax.inject.Inject

class TimerRepository @Inject constructor(
    private val dbRemote: TimerServices,
) : BaseRepository() {

    fun likeCurrentRecipe(
        recipe: SavedRecipeDomain,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.likeCurrentRecipe(recipe) }

    fun updateHistory(
        recipe: Recipe,
        brewDate: String,
        isLiked: Boolean,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.updateHistory(recipe, brewDate, isLiked) }

    fun addToHistory(
        recipe: Recipe,
        brewDate: String,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.addToHistory(recipe, brewDate) }

    fun isRecipeSaved(
        recipe: Recipe?,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.isRecipeSaved(recipe) }

    fun updateBrewingState(
        setBrewing: Boolean,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.updateBrewingState(setBrewing) }
}
