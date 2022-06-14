package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.HomeDatabase.Companion.getDatabase
import android.content.Context
import android.util.Log
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ISavedRecipesServices {
    suspend fun getListsFromDB(
//        listener: OnGetSavedListsFromDBFinishListener?,
        context: Context?
    ): List<SavedRecipeDomain>?

//    suspend fun deleteRecipeFromDB(
////        listener: OnGetRestFavouritesAfterDeletionFinishListener?,
//        recipe: SavedRecipeDomain?,
//        ctx: Context?
//    )
//
//    suspend fun getSpecificRecipeFromDB(
////        listener: OnGetSingleRecipeFromDBFinishListener?,
//        ctx: Context?,
//        recipe: SavedRecipeDomain?
//    )
}

class SavedRecipesServicesImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISavedRecipesServices {
    override suspend fun getListsFromDB(context: Context?): List<SavedRecipeDomain>?  {
        val database = getDatabase(context!!)
        return database.let { db ->
            withContext(dispatcher) {
                val recipesDao = db.savedRecipeDao()
                Log.d("recipesDao!!.sedRecipes", recipesDao.toString())
                return@withContext recipesDao.savedRecipes
            }
        }
    }

//    override suspend fun deleteRecipeFromDB(recipe: SavedRecipeDomain?, ctx: Context?) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getSpecificRecipeFromDB(ctx: Context?, recipe: SavedRecipeDomain?) {
//        TODO("Not yet implemented")
//    }
}

