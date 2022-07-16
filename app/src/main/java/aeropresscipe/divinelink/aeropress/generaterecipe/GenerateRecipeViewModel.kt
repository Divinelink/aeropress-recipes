package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class GenerateRecipeViewModel @AssistedInject constructor(
    private var repository: GenerateRecipeRepository,
    @Assisted override var delegate: WeakReference<IGenerateRecipeViewModel>? = null
) : BaseViewModel<IGenerateRecipeViewModel>(),
    GenerateRecipeIntents {
    internal var statesList: MutableList<GenerateRecipeState> = mutableListOf()

    private var dice: DiceDomain? = null

    var state: GenerateRecipeState = GenerateRecipeState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
            statesList.add(value)
        }

    init {
        state = GenerateRecipeState.InitialState
    }

    override fun getRecipe() {
        repository.getRecipe { dice ->
            state = GenerateRecipeState.ShowRecipeState(dice.recipe.buildSteps())
            state = when (dice.isBrewing) {
                true -> GenerateRecipeState.ShowResumeButtonState
                false -> GenerateRecipeState.HideResumeButtonState
            }

            this.dice = dice
        }
    }

    override fun generateRecipe() {
        repository.checkIfBrewing { isBrewing ->
            when (isBrewing) {
                true -> state = GenerateRecipeState.ShowAlreadyBrewingState
                false -> generateRandomRecipe()
            }
        }
    }

    override fun forceGenerateRecipe() {
        generateRandomRecipe()
        state = GenerateRecipeState.HideResumeButtonState
    }

    private fun generateRandomRecipe() {
        repository.createNewRecipe { dice ->
            state = GenerateRecipeState.RefreshRecipeState(dice.recipe.buildSteps())
            this.dice = dice
        }
    }

    override fun startTimer(resume: Boolean) {
        dice?.recipe?.isNewRecipe = resume
        dice?.recipe?.let { recipe -> state = GenerateRecipeState.StartTimerState(recipe) }
    }
}

interface IGenerateRecipeViewModel {
    fun updateState(state: GenerateRecipeState)
}

interface GenerateRecipeIntents : MVIBaseView {
    fun getRecipe()

    fun generateRecipe()
    fun forceGenerateRecipe()

    fun startTimer(resume: Boolean)
}

sealed class GenerateRecipeState {
    object InitialState : GenerateRecipeState()
    object LoadingState : GenerateRecipeState()
    data class ErrorState(val data: String) : GenerateRecipeState()

    object ShowAlreadyBrewingState : GenerateRecipeState()
    object ShowResumeButtonState : GenerateRecipeState()
    object HideResumeButtonState : GenerateRecipeState()
    data class ShowRecipeState(val steps: MutableList<RecipeStep>) : GenerateRecipeState()
    data class RefreshRecipeState(val steps: MutableList<RecipeStep>) : GenerateRecipeState()

    data class StartTimerState(val recipe: Recipe) : GenerateRecipeState()
}

interface GenerateRecipeStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState(state: GenerateRecipeState.ErrorState)

    fun handleShowResumeButtonState()
    fun handleHideResumeButtonState()

    fun handleShowAlreadyBrewingState()
    fun handleShowRecipeState(state: GenerateRecipeState.ShowRecipeState)
    fun handleRefreshRecipeState(state: GenerateRecipeState.RefreshRecipeState)
    fun handleStartTimerState(state: GenerateRecipeState.StartTimerState)
}

@Suppress("UNCHECKED_CAST")
class GenerateRecipeViewModelFactory(
    private val assistedFactory: GenerateRecipeViewModelAssistedFactory,
    private val delegate: WeakReference<IGenerateRecipeViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GenerateRecipeViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface GenerateRecipeViewModelAssistedFactory {
    fun create(delegate: WeakReference<IGenerateRecipeViewModel>?): GenerateRecipeViewModel
}
