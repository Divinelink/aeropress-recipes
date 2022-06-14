package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.BaseAndroidViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import android.app.Application
import java.lang.ref.WeakReference

class SavedRecipesViewModel(
    application: Application,
    override var delegate: WeakReference<ISavedRecipesViewModel>?,
    private var dbRepository: SavedRecipesRepository = SavedRecipesRepository(),
) : BaseAndroidViewModel<ISavedRecipesViewModel>(application), SavedRecipesIntents {
    internal var statesList: MutableList<SavedRecipesState> = mutableListOf()

    var state: SavedRecipesState = SavedRecipesState.InitialState
        set(value) {
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    override fun getSavedRecipes() {
        dbRepository.getListsFromDB(
            context = getApplication(),
            completionBlock = { recipes ->
                state = if (recipes.isNullOrEmpty()) {
                    SavedRecipesState.EmptyRecipesState
                } else {
                    SavedRecipesState.RecipesState(recipes)
                }
            }
        )
    }


//    fun getListsFromDB(
//        listener: OnGetSavedListsFromDBFinishListener?,
//        ctx: Context?
//    ) {
//
//    }

    override fun startBrew(recipe: SavedRecipeDomain?) {
        TODO("Not yet implemented")
    }

    override fun deleteRecipe(recipe: SavedRecipeDomain?) {
        TODO("Not yet implemented")
    }
}

interface ISavedRecipesViewModel {
    fun updateState(state: SavedRecipesState)
}

interface SavedRecipesIntents : MVIBaseView {

    fun getSavedRecipes()

    fun startBrew(recipe: SavedRecipeDomain?)

    fun deleteRecipe(recipe: SavedRecipeDomain?)

    //TODO add your code here

//
//    fun deleteRecipeFromDB(
//        recipe: SavedRecipeDomain?,
//        listener: OnGetRestFavouritesAfterDeletionFinishListener?,
//        ctx: Context?
//    )
//
//    fun getSpecificRecipeFromDB(
//        recipe: SavedRecipeDomain?
//        ctx: Context?,
//        listener: OnGetSingleRecipeFromDBFinishListener?,
//    )
}

sealed class SavedRecipesState {
    object InitialState : SavedRecipesState()
    object LoadingState : SavedRecipesState()
    data class ErrorState(val data: String) : SavedRecipesState()

    object EmptyRecipesState : SavedRecipesState()
    data class RecipesState(val recipes: List<SavedRecipeDomain>) : SavedRecipesState()
}

interface SavedRecipesStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()

    fun handleEmptyRecipesState()
    fun handleRecipesState(state: SavedRecipesState.RecipesState)

}

