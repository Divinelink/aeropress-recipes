package aeropresscipe.divinelink.aeropress.recipe

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.DISLIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView.Companion.LIKE_MAX_FRAME
import aeropresscipe.divinelink.aeropress.history.LikeSnackBar
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.recipe.models.RecipeStep
import aeropresscipe.divinelink.aeropress.recipe.models.buildSteps
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.divinelink.aeropress.recipes.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference

class RecipeViewModel @AssistedInject constructor(
    private var repository: RecipeRepository,
    private var timerRepository: TimerRepository,
    @Assisted public override var delegate: WeakReference<IRecipeViewModel>? = null,
) : BaseViewModel<IRecipeViewModel>(),
    RecipeIntents {
    internal var statesList: MutableList<RecipeState> = mutableListOf()
    private var dice: DiceDomain? = null

    var state: RecipeState = RecipeState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
            statesList.add(value)
        }

    override fun init(hour: Int) {
        state = RecipeState.InitialState
        state = RecipeState.UpdateToolbarState(getGreetingMessage(hour))
    }

    override fun getRecipe(refresh: Boolean) {
        if (dice == null || refresh) {
            repository.getRecipe { dice ->
                Timber.d("Fetching dice from database.")
                updateRecipeStates(dice)
                this.dice = dice
            }
        } else {
            Timber.d("Getting cached dice.")
            dice?.let { updateRecipeStates(it) }
        }
    }

    private fun updateRecipeStates(dice: DiceDomain) {
        state = RecipeState.ShowRecipeState(dice.recipe.buildSteps())
        timerRepository.isRecipeSaved(dice.recipe) { saved ->
            val frame = when (saved) {
                true -> LIKE_MAX_FRAME
                false -> DISLIKE_MAX_FRAME
            }
            state = RecipeState.UpdateSavedIndicator(frame)
        }
    }

    override fun generateRecipe() {
        viewModelScope.launch {
            repository.checkIfBrewing { isBrewing ->
                when (isBrewing) {
                    true -> state = RecipeState.ShowAlreadyBrewingState
                    false -> generateRandomRecipe()
                }
            }
        }
    }

    override fun forceGenerateRecipe() {
        generateRandomRecipe()
        state = RecipeState.HideResumeButtonState
    }

    private fun generateRandomRecipe() {
        repository.createNewRecipe { dice ->
            state = RecipeState.RefreshRecipeState(dice.recipe.buildSteps())
            state = RecipeState.UpdateSavedIndicator(DISLIKE_MAX_FRAME)
            this.dice = dice
        }
    }

    override fun startTimer(resume: Boolean) {
        val flow = when (resume) {
            true -> TimerFlow.RESUME
            false -> TimerFlow.START
        }
        dice?.recipe?.let { recipe -> state = RecipeState.StartTimerState(recipe, flow) }
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
            timerRepository.likeRecipe(it) { recipeLiked ->
                if (recipeLiked) {
                    state = RecipeState.RecipeSavedState
                    state = RecipeState.ShowSnackBar(LikeSnackBar.Like)
                } else {
                    state = RecipeState.RecipeRemovedState
                    state = RecipeState.ShowSnackBar(LikeSnackBar.Remove)
                }
            }
        }
    }

    companion object {
        val MORNING_RANGE: IntRange = 0..11
        val AFTERNOON_RANGE: IntRange = 12..15
    }
}

interface IRecipeViewModel {
    fun updateState(state: RecipeState)
}

interface RecipeIntents : MVIBaseView {
    fun init(hour: Int)
    fun getRecipe(refresh: Boolean)

    fun generateRecipe()
    fun forceGenerateRecipe()

    fun startTimer(resume: Boolean)

    fun likeRecipe()
}

sealed class RecipeState {
    object InitialState : RecipeState()

    object ShowAlreadyBrewingState : RecipeState()
    object HideResumeButtonState : RecipeState()

    data class UpdateToolbarState(@StringRes val title: Int) : RecipeState()

    data class ShowRecipeState(val steps: MutableList<RecipeStep>) : RecipeState()
    data class RefreshRecipeState(val steps: MutableList<RecipeStep>) : RecipeState()

    data class StartTimerState(val recipe: Recipe, val flow: TimerFlow) : RecipeState()

    data class UpdateSavedIndicator(val frame: Int) : RecipeState()
    data class ShowSnackBar(val value: LikeSnackBar) : RecipeState()

    object RecipeSavedState : RecipeState()
    object RecipeRemovedState : RecipeState()
}

interface GenerateRecipeStateHandler {
    fun handleInitialState()

    fun handleHideResumeButtonState()

    fun handleUpdateToolbarState(state: RecipeState.UpdateToolbarState)

    fun handleShowAlreadyBrewingState()
    fun handleShowRecipeState(state: RecipeState.ShowRecipeState)
    fun handleRefreshRecipeState(state: RecipeState.RefreshRecipeState)
    fun handleStartTimerState(state: RecipeState.StartTimerState)

    fun handleUpdateSavedIndicator(state: RecipeState.UpdateSavedIndicator)
    fun handleShowSnackBar(state: RecipeState.ShowSnackBar)
    fun handleRecipeSavedState()
    fun handleRecipeRemovedState()
}

@Suppress("UNCHECKED_CAST")
class RecipeViewModelFactory(
    private val assistedFactory: RecipeViewModelAssistedFactory,
    private val delegate: WeakReference<IRecipeViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface RecipeViewModelAssistedFactory {
    fun create(delegate: WeakReference<IRecipeViewModel>?): RecipeViewModel
}
