package aeropresscipe.divinelink.aeropress.favorites

import aeropresscipe.divinelink.aeropress.favorites.domain.usecase.DeleteFavoriteUseCase
import aeropresscipe.divinelink.aeropress.favorites.domain.usecase.FetchAllFavoritesUseCase
import aeropresscipe.divinelink.aeropress.favorites.ui.FavoritesViewState
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val fetchAllFavoritesUseCase: FetchAllFavoritesUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(FavoritesViewState())
    val viewState = _viewState.asStateFlow()

    init {
        fetchFavorites()
    }

    // Fixme brewRecipe needs to be null once user resumes to this screen
    // This can also be fixed by sending a one time event - using Shared Flow, but tbh I want to this viewModel to be single state.
    fun startBrewClicked(recipe: Recipe) {
        _viewState.update {
            it.copy(
                isLoading = false,
                brewRecipe = recipe,
                errorMessage = null
            )
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            deleteFavoriteUseCase(recipe)
        }
    }

    fun refresh() {
        Timber.d("Refreshing favorites.")
        fetchFavorites()
    }

    private fun getFavoritesViewState(result: Result<List<Favorites>>) {
        when (result) {
            is Result.Error -> {
                _viewState.update {
                    FavoritesViewState(
                        isLoading = false,
                        errorMessage = result.exception.toString()
                    )
                }
            }
            is Result.Loading -> _viewState.update { FavoritesViewState() }
            is Result.Success -> {
                if (result.data.isEmpty()) {
                    _viewState.update {
                        FavoritesViewState(
                            isLoading = false,
                            emptyRecipes = true,
                        )
                    }
                } else {
                    _viewState.update {
                        FavoritesViewState(
                            isLoading = false,
                            emptyRecipes = false,
                            recipes = result.data,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            fetchAllFavoritesUseCase.invoke(Unit).collect { result ->
                getFavoritesViewState(result)
            }
        }
    }
}
