package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.BaseAndroidViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI
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

    override fun startBrew(recipe: SavedRecipeDomain) {
        dbRepository.startBrew(
            recipe = recipe,
            context = getApplication(),
            completionBlock = { selectedRecipe ->
                state = if (selectedRecipe == null) {
                    SavedRecipesState.ErrorState("Something went wrong!") // //TODO 15/6/22 divinelink: Fix Error State
                } else {
                    val newRecipe = DiceUI(
                        selectedRecipe.bloomTime,
                        selectedRecipe.brewTime,
                        selectedRecipe.bloomWater,
                        selectedRecipe.brewWaterAmount,
                        isNewRecipe = true
                    )
                    SavedRecipesState.StartNewBrewState(newRecipe)
                }
            }
        )
    }

    override fun deleteRecipe(recipe: SavedRecipeDomain) {
        dbRepository.deleteRecipe(
            recipe = recipe,
            context = getApplication(),
            completionBlock = { recipes ->
                state = if (recipes == null) {
                    SavedRecipesState.ErrorState("Something went wrong!") // todo fix error state
                } else if (recipes.isEmpty()) {
                    SavedRecipesState.EmptyRecipesState
                } else {
                    SavedRecipesState.RecipeDeletedState(recipes)
                }
            }
        )
    }
}

interface ISavedRecipesViewModel {
    fun updateState(state: SavedRecipesState)
}

interface SavedRecipesIntents : MVIBaseView {
    fun getSavedRecipes()
    fun startBrew(recipe: SavedRecipeDomain)
    fun deleteRecipe(recipe: SavedRecipeDomain)
}

sealed class SavedRecipesState {
    object InitialState : SavedRecipesState()
    object LoadingState : SavedRecipesState()
    data class ErrorState(val data: String) : SavedRecipesState()

    object EmptyRecipesState : SavedRecipesState()
    data class RecipesState(val recipes: List<SavedRecipeDomain>) : SavedRecipesState()
    data class RecipeDeletedState(val recipes: List<SavedRecipeDomain>) : SavedRecipesState()
    data class StartNewBrewState(val recipe: DiceUI) : SavedRecipesState()
}

interface SavedRecipesStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()

    fun handleEmptyRecipesState()
    fun handleRecipesState(state: SavedRecipesState.RecipesState)
    fun handleRecipeDeletedState(state: SavedRecipesState.RecipeDeletedState)
    fun handleStartNewBrew(state: SavedRecipesState.StartNewBrewState)
}
