package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import javax.inject.Inject

class TimerRepository @Inject constructor(
    private val dbRemote: TimerServices,
) : BaseRepository() {

    fun likeCurrentRecipe(
        recipe: Recipe,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.likeCurrentRecipe(recipe) }

    fun updateHistory(
        recipe: Recipe,
        isLiked: Boolean,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.updateHistory(recipe, isLiked) }

    fun addToHistory(
        recipe: Recipe,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.addToHistory(recipe) }

    fun isRecipeSaved(
        recipe: Recipe?,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.isRecipeSaved(recipe) }

    fun updateBrewingState(
        setBrewing: Boolean,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.updateBrewingState(setBrewing) }

    fun updateTimes(
        bloomEnds: Long,
        brewEnds: Long,
    ) = performTransaction { dbRemote.updateTimes(bloomEnds, brewEnds) }

    fun resume(
        completionBlock: (DiceDomain) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getResumeTimes() }
}
