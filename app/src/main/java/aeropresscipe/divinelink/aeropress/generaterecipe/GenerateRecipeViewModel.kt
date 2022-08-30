package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.LIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.generaterecipe.models.RecipeStep
import aeropresscipe.divinelink.aeropress.generaterecipe.models.buildSteps
import aeropresscipe.divinelink.aeropress.history.LikeSnackBar
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class GenerateRecipeViewModel @AssistedInject constructor(
    private var repository: GenerateRecipeRepository,
    private var timerRepository: TimerRepository,
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

    override fun init(hour: Int) {
        state = GenerateRecipeState.InitialState
        state = GenerateRecipeState.UpdateToolbarState(getGreetingMessage(hour))
    }

    override fun getRecipe() {
        if (dice == null) {
            repository.getRecipe { dice ->
                Timber.d("Fetching dice from database.")
                updateRecipeStates(dice)
                this.dice = dice
            }
        } else {
            Timber.d("Getting cached dice.")
            dice?.let { it -> updateRecipeStates(it) }
        }
    }

    private fun updateRecipeStates(dice: DiceDomain) {
        state = GenerateRecipeState.ShowRecipeState(dice.recipe.buildSteps())
        state = when (dice.isBrewing) {
            true -> GenerateRecipeState.ShowResumeButtonState
            false -> GenerateRecipeState.HideResumeButtonState
        }
        timerRepository.isRecipeSaved(dice.recipe) { saved ->
            val frame = when (saved) {
                true -> LIKE_MAX_FRAME
                false -> DISLIKE_MAX_FRAME
            }
            state = GenerateRecipeState.UpdateSavedIndicator(frame)
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
            state = GenerateRecipeState.UpdateSavedIndicator(DISLIKE_MAX_FRAME)
            this.dice = dice
        }
    }

    override fun startTimer(resume: Boolean) {
        val flow = when (resume) {
            true -> TimerFlow.RESUME
            false -> TimerFlow.START
        }
        dice?.recipe?.isNewRecipe = resume
        dice?.recipe?.let { recipe -> state = GenerateRecipeState.StartTimerState(recipe, flow) }
    }

    @StringRes
    private fun getGreetingMessage(hour: Int): Int {
        return when (hour) {
            in MORNING_RANGE -> R.string.good_morning
            in AFTERNOON_RANGE -> R.string.good_afternoon
            else -> R.string.good_evening
        }
    }

    override fun likeRecipe() {
        dice?.recipe?.let {
            timerRepository.likeCurrentRecipe(it) { recipeLiked ->
                if (recipeLiked) {
                    state = GenerateRecipeState.RecipeSavedState
                    state = GenerateRecipeState.ShowSnackBar(LikeSnackBar.Like)
                } else {
                    state = GenerateRecipeState.RecipeRemovedState
                    state = GenerateRecipeState.ShowSnackBar(LikeSnackBar.Remove)
                }
            }
        }
    }

    companion object {
        val MORNING_RANGE: IntRange = 0..11
        val AFTERNOON_RANGE: IntRange = 12..15
    }
}

interface IGenerateRecipeViewModel {
    fun updateState(state: GenerateRecipeState)
}

interface GenerateRecipeIntents : MVIBaseView {
    fun init(hour: Int)
    fun getRecipe()

    fun generateRecipe()
    fun forceGenerateRecipe()

    fun startTimer(resume: Boolean)

    fun likeRecipe()
}

sealed class GenerateRecipeState {
    object InitialState : GenerateRecipeState()
    object LoadingState : GenerateRecipeState()
    data class ErrorState(val data: String) : GenerateRecipeState()

    object ShowAlreadyBrewingState : GenerateRecipeState()
    object ShowResumeButtonState : GenerateRecipeState()
    object HideResumeButtonState : GenerateRecipeState()

    data class UpdateToolbarState(@StringRes val title: Int) : GenerateRecipeState()

    data class ShowRecipeState(val steps: MutableList<RecipeStep>) : GenerateRecipeState()
    data class RefreshRecipeState(val steps: MutableList<RecipeStep>) : GenerateRecipeState()

    data class StartTimerState(val recipe: Recipe, val flow: TimerFlow) : GenerateRecipeState()

    data class UpdateSavedIndicator(val frame: Int) : GenerateRecipeState()
    data class ShowSnackBar(val value: LikeSnackBar) : GenerateRecipeState()

    object RecipeSavedState : GenerateRecipeState()
    object RecipeRemovedState : GenerateRecipeState()
}

interface GenerateRecipeStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState(state: GenerateRecipeState.ErrorState)

    fun handleShowResumeButtonState()
    fun handleHideResumeButtonState()

    fun handleUpdateToolbarState(state: GenerateRecipeState.UpdateToolbarState)

    fun handleShowAlreadyBrewingState()
    fun handleShowRecipeState(state: GenerateRecipeState.ShowRecipeState)
    fun handleRefreshRecipeState(state: GenerateRecipeState.RefreshRecipeState)
    fun handleStartTimerState(state: GenerateRecipeState.StartTimerState)

    fun handleUpdateSavedIndicator(state: GenerateRecipeState.UpdateSavedIndicator)
    fun handleShowSnackBar(state: GenerateRecipeState.ShowSnackBar)
    fun handleRecipeSavedState()
    fun handleRecipeRemovedState()
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
