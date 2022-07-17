package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.generaterecipe.models.getBrewingStates
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import aeropresscipe.divinelink.aeropress.timer.util.BrewPhase
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.Phase
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import android.annotation.SuppressLint
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
        val brewPhase = BrewPhase.Builder()
            .brewStates(transferableModel.recipe?.getBrewingStates())
            .brewState(transferableModel.recipe?.getBrewingStates()?.firstOrNull())
            .build()
        this.transferableModel?.brew = brewPhase

        repository.isRecipeSaved(transferableModel.recipe) { saved ->
            val image = when (saved) {
                true -> R.drawable.ic_heart_on
                false -> R.drawable.ic_heart_off
            }
            state = TimerState.UpdateSavedIndicator(image)
        }
    }

    override fun resume() {
        repository.resume {
            if (it != null) {
                val states = transferableModel?.recipe?.getBrewingStates()
                val bloomTimeLeft = it.bloomTimeLeft - System.currentTimeMillis()
                val brewTimeLeft = it.brewTimeLeft - System.currentTimeMillis()

                if (bloomTimeLeft > 0) {
                    val bloomState = states?.firstOrNull { state -> state.phase == Phase.Bloom }
                    transferableModel?.currentBrewState = bloomState
                    startTimerStates(bloomState!!, bloomTimeLeft)
                } else if (brewTimeLeft > 0) {
                    val brewState = states?.firstOrNull { state -> state.phase == Phase.Brew }
                    transferableModel?.currentBrewState = brewState
                    startTimerStates(brewState!!, brewTimeLeft)
                } else {
                    state = TimerState.FinishState
                }
            }
        }
    }

    private fun startTimerStates(brewState: BrewState, timeLeft: Long, animate: Boolean = false) {
        state = TimerState.StartTimer(
            water = brewState.brewWater,
            title = brewState.title,
            description = brewState.description
        )
        state = TimerState.StartProgressBar(
            maxValue = brewState.brewTime.inMilliseconds().toInt(),
            timeInMilliseconds = timeLeft,
            animate = animate
        )
    }

    override fun startBrew() {
        if (transferableModel?.recipe == null) {
            state = TimerState.ErrorState("Something went wrong!")
        } else {
            // Initialise Timer
            repository.addToHistory(
                recipe = transferableModel?.recipe!!,
                brewDate = getCurrentDate(),
                completionBlock = {
                    startTimers()
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
            // Update current brew state
            transferableModel?.currentBrewState = brew.getCurrentState()
            if (brew.getCurrentState() == BrewState.Finished) {
                repository.updateBrewingState(false) {
                    Timber.d("Recipe finished brewing")
                }
                state = TimerState.FinishState
            } else {
                startTimerStates(brew.getCurrentState(), brew.getCurrentState().brewTime.inMilliseconds(), animate = true)
            }
        }
    }

    override fun exitTimer(millisecondsLeft: Long) {
        val bloomEnds: Long
        val brewEnds: Long
        when (transferableModel?.currentBrewState) {
            is BrewState.Bloom -> {
                bloomEnds = millisecondsLeft + System.currentTimeMillis()
                brewEnds = transferableModel?.recipe?.brewTime?.inMilliseconds()?.plus(millisecondsLeft)?.plus(System.currentTimeMillis()) ?: 0L
                repository.updateTimes(bloomEnds = bloomEnds, brewEnds = brewEnds)
            }
            is BrewState, is BrewState.BrewWithBloom -> {
                bloomEnds = 0
                brewEnds = millisecondsLeft + System.currentTimeMillis()
                repository.updateTimes(bloomEnds = bloomEnds, brewEnds = brewEnds)
            }
            else -> {
                repository.updateTimes(bloomEnds = 0L, brewEnds = 0L)
                repository.updateBrewingState(false) {
                    Timber.d("Recipe brewing set to false.")
                }
            }
        }
    }

    private fun startTimers() {
        val brewState = transferableModel?.brew?.brewState
        val brewTime = transferableModel?.brew?.brewState?.brewTime
        transferableModel?.currentBrewState = brewState
        if (brewState != null && brewTime != null) {
            startTimerStates(brewState, brewTime.inMilliseconds())
        } else {
            state = TimerState.ErrorState("Something went wrong!")
        }
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
}

interface ITimerViewModel {
    fun updateState(state: TimerState)
}

interface TimerIntents : MVIBaseView {
    fun init(transferableModel: TimerTransferableModel)
    fun startBrew()
    fun resume()
    fun saveRecipe(recipe: Recipe?)
    fun exitTimer(millisecondsLeft: Long)
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

    data class StartProgressBar(val maxValue: Int, val timeInMilliseconds: Long, val animate: Boolean) : TimerState()

    object ExitState : TimerState()

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

    fun handleExitState()

    fun handleFinishState()
}
