package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.generaterecipe.models.getBrewingStates
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import aeropresscipe.divinelink.aeropress.timer.util.BrewPhase
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import gr.divinelink.core.util.extensions.inMilliseconds
import timber.log.Timber
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date

class TimerViewModel @AssistedInject constructor(
    private var repository: TimerRepository,
    @Assisted override var delegate: WeakReference<ITimerViewModel>? = null,
) : BaseViewModel<ITimerViewModel>(),
    TimerIntents {
    internal var statesList: MutableList<TimerState> = mutableListOf()

    private var transferableModel: TimerTransferableModel? = null

    var state: TimerState = TimerState.InitialState
        set(value) {
            Timber.d(state.javaClass.simpleName)
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    override fun init(transferableModel: TimerTransferableModel) {
        this.transferableModel = transferableModel
        repository.isRecipeSaved(transferableModel.recipe) { saved ->
            val image = when (saved) {
                true -> R.drawable.ic_heart_on
                false -> R.drawable.ic_heart_off
            }
            state = TimerState.UpdateSavedIndicator(image)
        }
    }

    override fun startBrew(transferableModel: TimerTransferableModel?) {
        if (transferableModel?.recipe == null) {
            state = TimerState.ErrorState("Something went wrong!")
        } else {
            // Initialise Timer
            repository.addToHistory(
                recipe = transferableModel.recipe!!,
                brewDate = getCurrentDate(),
                completionBlock = {
                    startTimers(transferableModel)
                }
            )

            repository.updateBrewingState(true) {
                Timber.d("Recipe is brewing")
            }
        }
    }

    override fun updateTimer() {
        transferableModel?.brew?.removeCurrentPhase()
        transferableModel?.brew?.let { brew ->
            if (brew.getCurrentState() == BrewState.Finished) {
                repository.updateBrewingState(false) {
                    Log.d(TAG, "Recipe finished brewing")
                }
                state = TimerState.FinishState
            } else {
                state = TimerState.StartTimer(
                    water = brew.getCurrentState().brewWater,
                    title = brew.getCurrentState().title,
                    description = brew.getCurrentState().description
                )
                state = TimerState.StartProgressBar(
                    timeInMilliseconds = brew.getCurrentState().brewTime.inMilliseconds(),
                    animate = true
                )
            }
        }
    }

    private fun startTimers(transferableModel: TimerTransferableModel?) {
        val brewPhase = BrewPhase.Builder()
            .brewStates(transferableModel?.recipe?.getBrewingStates())
            .brewState(transferableModel?.recipe?.getBrewingStates()?.firstOrNull())
            .build()

        transferableModel?.brew = brewPhase

        state = TimerState.StartTimer(
            water = brewPhase.brewState.brewWater,
            title = brewPhase.brewState.title,
            description = brewPhase.brewState.description
        )

        state = TimerState.StartProgressBar(
            timeInMilliseconds = brewPhase.brewState.brewTime.inMilliseconds(),
            animate = false
        )
    }

    override fun saveRecipe(recipe: Recipe?) {
        if (recipe == null) {
            state = TimerState.ErrorState("Something went wrong!") // Fixme
        } else {
            // Save Recipe on DB
            val savedRecipe = SavedRecipeDomain(recipe, getCurrentDate())
            repository.likeCurrentRecipe(
                recipe = savedRecipe,
                completionBlock = { recipeLiked ->
                    if (recipeLiked) {
                        state = TimerState.RecipeSavedState
                        repository.updateHistory(recipe, getCurrentDate(), recipeLiked) { /* Intentionally Blank. */ }
                    } else {
                        state = TimerState.RecipeRemovedState
                        repository.updateHistory(recipe, getCurrentDate(), recipeLiked) { /* Intentionally Blank. */ }
                    }
                }
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        return formatter.format(date.time)
    }

    companion object {
        private val TAG: String? = TimerViewModel::class.simpleName
    }
}

interface ITimerViewModel {
    fun updateState(state: TimerState)
}

interface TimerIntents : MVIBaseView {
    fun init(transferableModel: TimerTransferableModel)
    fun startBrew(transferableModel: TimerTransferableModel?)
    fun saveRecipe(recipe: Recipe?)

    fun updateTimer()
}

sealed class TimerState {
    object InitialState : TimerState()
    object LoadingState : TimerState()
    data class ErrorState(val data: String) : TimerState()

    object RecipeSavedState : TimerState()
    object RecipeRemovedState : TimerState()

    data class UpdateSavedIndicator(@DrawableRes val image: Int) : TimerState()

    data class StartTimer(
        val water: Int,
        @StringRes val title: Int,
        @StringRes val description: Int
    ) : TimerState()

    data class StartProgressBar(val timeInMilliseconds: Long, val animate: Boolean) : TimerState()

    object FinishState : TimerState()
}

interface TimerStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState(state: TimerState.ErrorState)

    fun handleRecipeSavedState()
    fun handleRecipeRemovedState()

    fun handleUpdateSavedIndicator(state: TimerState.UpdateSavedIndicator)

    fun handleStartTimer(state: TimerState.StartTimer)
    fun handleStartProgressBar(state: TimerState.StartProgressBar)

    fun handleFinishState()
}
