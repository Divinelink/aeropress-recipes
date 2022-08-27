package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.lang.ref.WeakReference


class SavedRecipesViewModel @AssistedInject constructor(
    @Assisted override var delegate: WeakReference<ISavedRecipesViewModel>?,
    private var dbRepository: SavedRecipesRepository
) : BaseViewModel<ISavedRecipesViewModel>(), SavedRecipesIntents {
    internal var statesList: MutableList<SavedRecipesState> = mutableListOf()

    var state: SavedRecipesState = SavedRecipesState.InitialState
        set(value) {
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    init {
        state = SavedRecipesState.InitialState
        dbRepository.getListsFromDB(
            completionBlock = { recipes ->
                state = if (recipes.isNullOrEmpty()) {
                    SavedRecipesState.EmptyRecipesState
                } else {
                    SavedRecipesState.RecipesState(recipes)
                }
            }
        )
    }

    override fun startBrew(recipe: Recipe) {
        dbRepository.startBrew(
            recipe = recipe,
            completionBlock = { selectedRecipe ->
                state = if (selectedRecipe == null) {
                    SavedRecipesState.ErrorState("Something went wrong!") // //TODO 15/6/22 divinelink: Fix Error State
                } else {
                    recipe.isNewRecipe = true
                    SavedRecipesState.StartNewBrewState(recipe)
                }
            }
        )
    }

    override fun deleteRecipe(recipe: Recipe) {
        dbRepository.deleteRecipe(
            recipe = recipe,
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
    fun startBrew(recipe: Recipe)
    fun deleteRecipe(recipe: Recipe)
}

sealed class SavedRecipesState {
    object InitialState : SavedRecipesState()
    object LoadingState : SavedRecipesState()
    data class ErrorState(val data: String) : SavedRecipesState()

    object EmptyRecipesState : SavedRecipesState()
    data class RecipesState(val recipes: List<SavedRecipeDomain>) : SavedRecipesState()
    data class RecipeDeletedState(val recipes: List<SavedRecipeDomain>) : SavedRecipesState()
    data class StartNewBrewState(val recipe: Recipe) : SavedRecipesState()
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


@Suppress("UNCHECKED_CAST")
class SavedRecipesViewModelFactory(
    private val assistedFactory: SavedTimerViewModelAssistedFactory,
    private val delegate: WeakReference<ISavedRecipesViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedRecipesViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface SavedTimerViewModelAssistedFactory {
    fun create(delegate: WeakReference<ISavedRecipesViewModel>?): SavedRecipesViewModel
}

