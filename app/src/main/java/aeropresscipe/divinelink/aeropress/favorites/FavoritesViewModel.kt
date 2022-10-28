package aeropresscipe.divinelink.aeropress.favorites

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class FavoritesViewModel @AssistedInject constructor(
    @Assisted public override var delegate: WeakReference<IFavoritesViewModel>?,
    private var dbRepository: FavoritesRepository
) : BaseViewModel<IFavoritesViewModel>(), FavoritesIntents {
    internal var statesList: MutableList<FavoritesState> = mutableListOf()

    var state: FavoritesState = FavoritesState.InitialState
        set(value) {
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    init {
        state = FavoritesState.InitialState
        fetchFavorites()
    }

    override fun startBrew(recipe: Recipe) {
        state = FavoritesState.StartNewBrewState(recipe)
    }

    override fun deleteRecipe(recipe: Recipe) {
        dbRepository.deleteRecipe(
            recipe = recipe,
            completionBlock = { recipes ->
                state = if (recipes == null) {
                    FavoritesState.ErrorState("Something went wrong!") // todo fix error state
                } else if (recipes.isEmpty()) {
                    FavoritesState.EmptyRecipesState
                } else {
                    FavoritesState.RecipeDeletedState(recipes)
                }
            }
        )
    }

    override fun refresh() {
        Timber.d("Refreshing favorites.")
        fetchFavorites()
    }

    private fun fetchFavorites() {
        dbRepository.getFavorites(
            completionBlock = { recipes ->
                state = if (recipes.isNullOrEmpty()) {
                    FavoritesState.EmptyRecipesState
                } else {
                    FavoritesState.RecipesState(recipes)
                }
            }
        )
    }
}

interface IFavoritesViewModel {
    fun updateState(state: FavoritesState)
}

interface FavoritesIntents : MVIBaseView {
    fun startBrew(recipe: Recipe)
    fun deleteRecipe(recipe: Recipe)
    fun refresh()
}

sealed class FavoritesState {
    object InitialState : FavoritesState()
    object LoadingState : FavoritesState()
    data class ErrorState(val data: String) : FavoritesState()

    object EmptyRecipesState : FavoritesState()
    data class RecipesState(val recipes: List<Favorites>) : FavoritesState()
    data class RecipeDeletedState(val recipes: List<Favorites>) : FavoritesState()
    data class StartNewBrewState(val recipe: Recipe) : FavoritesState()
}

interface FavoritesStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()

    fun handleEmptyRecipesState()
    fun handleRecipesState(state: FavoritesState.RecipesState)
    fun handleRecipeDeletedState(state: FavoritesState.RecipeDeletedState)
    fun handleStartNewBrew(state: FavoritesState.StartNewBrewState)
}

@Suppress("UNCHECKED_CAST")
class FavoritesViewModelFactory(
    private val assistedFactory: FavoritesViewModelAssistedFactory,
    private val delegate: WeakReference<IFavoritesViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface FavoritesViewModelAssistedFactory {
    fun create(delegate: WeakReference<IFavoritesViewModel>?): FavoritesViewModel
}
