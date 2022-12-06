package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import javax.inject.Inject

interface TimerRepository {

    fun likeRecipe(
        recipe: Recipe,
        completionBlock: (Boolean) -> Unit,
    )

    fun addToHistory(
        recipe: Recipe,
        completionBlock: () -> Unit,
    )

    fun isRecipeSaved(
        recipe: Recipe?,
        completionBlock: (Boolean) -> Unit,
    )

    fun updateBrewingState(
        setBrewing: Boolean,
        timeStartedMillis: Long,
        completionBlock: () -> Unit,
    )

    fun resume(
        completionBlock: (DiceDomain) -> Unit,
    )
}

class RoomTimerRepository @Inject constructor(
    private val dbRemote: TimerServices,
) : BaseRepository(), TimerRepository {

    override fun likeRecipe(
        recipe: Recipe,
        completionBlock: (Boolean) -> Unit,
    ) {
        performTransaction(completionBlock) { dbRemote.likeRecipe(recipe) }
    }

    override fun addToHistory(
        recipe: Recipe,
        completionBlock: () -> Unit,
    ) {
        performTransaction(
            completionBlock
        ) { dbRemote.addToHistory(recipe) }
    }

    override fun isRecipeSaved(
        recipe: Recipe?,
        completionBlock: (Boolean) -> Unit,
    ) {
        performTransaction(completionBlock) { dbRemote.isRecipeSaved(recipe) }
    }

    override fun updateBrewingState(
        setBrewing: Boolean,
        timeStartedMillis: Long,
        completionBlock: () -> Unit,
    ) {
        performTransaction(completionBlock) { dbRemote.updateBrewingState(setBrewing, timeStartedMillis) }
    }

    override fun resume(
        completionBlock: (DiceDomain) -> Unit,
    ) {
        performTransaction(completionBlock) { dbRemote.getResumeTimes() }
    }
}
