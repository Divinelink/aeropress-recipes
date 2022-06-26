package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class TimerRepository(
    override var coroutineContext: CoroutineContext = Dispatchers.Main,
    private val dbRemote: ITimerServices = TimerServices()
) : BaseRepository() {

    fun likeCurrentRecipe(
        recipe: SavedRecipeDomain,
        context: Context,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.likeCurrentRecipe(recipe, context) }

    fun updateHistory(
        recipe: DiceDomain,
        brewDate: String,
        isLiked: Boolean,
        context: Context,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.updateHistory(recipe, brewDate, isLiked, context) }

    fun addToHistory(
        recipe: DiceDomain,
        brewDate: String,
        context: Context,
        completionBlock: () -> Unit
    ) = performTransaction(completionBlock) { dbRemote.addToHistory(recipe, brewDate, context) }

    fun isRecipeSaved(
        recipe: DiceDomain?,
        context: Context,
        completionBlock: (Boolean) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.isRecipeSaved(recipe, context) }

}
