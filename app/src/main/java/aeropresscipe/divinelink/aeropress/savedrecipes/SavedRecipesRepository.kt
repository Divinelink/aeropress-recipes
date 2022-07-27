package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.logic.BaseRepository
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class SavedRecipesRepository(
    override var coroutineContext: CoroutineContext = Dispatchers.Main,
    private val dbRemote: ISavedRecipesServices = SavedRecipesServicesImpl()
) : BaseRepository() {

    fun getListsFromDB(
        context: Context,
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getRecipesFromDB(context) }

    fun deleteRecipe(
        recipe: Recipe,
        context: Context,
        completionBlock: (List<SavedRecipeDomain>?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.deleteRecipe(recipe, context) }

    fun startBrew(
        recipe: Recipe,
        context: Context,
        completionBlock: (Recipe?) -> Unit
    ) = performTransaction(completionBlock) { dbRemote.getSingleRecipe(recipe, context) }
}
