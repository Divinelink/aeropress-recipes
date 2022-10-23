package aeropresscipe.divinelink.aeropress.timer

// import aeropresscipe.divinelink.aeropress.base.di.Preferences
import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.recipe.models.getBrewTimeLeft
import aeropresscipe.divinelink.aeropress.recipe.models.getBrewingStates
import aeropresscipe.divinelink.aeropress.timer.util.BrewPhase
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.Phase
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import gr.divinelink.core.util.extensions.inMilliseconds
import timber.log.Timber
import java.lang.ref.WeakReference

class TimerViewModel @AssistedInject constructor(
    private var repository: TimerRepository,
    @Assisted public override var delegate: WeakReference<ITimerViewModel>? = null
) : BaseViewModel<ITimerViewModel>(),
    TimerIntents {
    internal var statesList: MutableList<TimerState> = mutableListOf()

//    @Inject
//    lateinit var preferences: Preferences

    internal var transferableModel: TimerTransferableModel? = null

    private var isBrewing: Boolean = false

    var state: TimerState = TimerState.InitialState
        set(value) {
            Timber.d(state.javaClass.simpleName)
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    override fun init(transferableModel: TimerTransferableModel) {
        state = TimerState.InitialState
        this.transferableModel = transferableModel
        val brewPhase = BrewPhase.Builder()
            .brewStates(transferableModel.recipe?.getBrewingStates())
            .build()
        this.transferableModel?.brew = brewPhase
    }

    override fun resume() {
        repository.resume {
            val states = transferableModel?.brew?.brewStates
            val timeLeft: Pair<Long, Long> = it.getBrewTimeLeft()
            val bloomState = states?.find { state -> state.phase == Phase.Bloom }

            if (timeLeft.first > 0) {
                startTimerStates(bloomState!!, timeLeft.first, false)
            } else if (timeLeft.second > 0) {
                // Remove bloomState from list and get BrewState for new timer.
                states?.remove(bloomState)
                val brewState = states?.find { state -> state.phase == Phase.Brew }
                startTimerStates(brewState!!, timeLeft.second, false)
            } else {
                finishRecipeBrewing()
            }

            repository.addToHistory(it.recipe) {
                // Do nothing
            }
        }
    }

    private fun finishRecipeBrewing() {
        repository.updateBrewingState(false, 0L) {
            state = TimerState.FinishState
        }
    }

    private fun startTimerStates(brewState: BrewState?, timeLeft: Long, animate: Boolean) {
        if (brewState == null) return
        state = TimerState.UpdateDescriptionState(brewState, animate)
        state = TimerState.UpdateProgressBar(
            maxValue = brewState.brewTime.inMilliseconds().toInt(),
            timeInMilliseconds = timeLeft,
            animate = animate,
            update = brewState.update
        )
    }

    override fun startBrew() {
        if (isBrewing) {
            resume()
        } else {
            if (transferableModel?.recipe == null) {
                state = TimerState.ErrorState("Something went wrong!")
            } else {
                // Initialise Timer
                repository.addToHistory(
                    recipe = transferableModel?.recipe!!,
                    completionBlock = {
                        startTimers()
                    }
                )
                repository.updateBrewingState(true, System.currentTimeMillis()) {
                    Timber.d("Recipe is brewing.")
                }
            }
            isBrewing = true
        }
    }

    override fun updateTimer() {
        val brew = transferableModel?.brew
        brew?.removeCurrentPhase()
        // Update current brew state
        if (brew?.getCurrentState() == BrewState.Finished) {
            finishRecipeBrewing()
        } else {
            startTimerStates(brew?.getCurrentState(), brew?.getCurrentState()?.brewTime.inMilliseconds(), true)
        }
//        if (!preferences.muteSound) {
//            state = TimerState.PlaySoundState
//        }
    }

    override fun exitTimer() {
        if (transferableModel?.brew?.getCurrentState() is BrewState.Finished) {
            repository.updateBrewingState(false, 0L) {
                Timber.d("Recipe is not brewing.")
            }
        }
        state = TimerState.ExitState
    }

    private fun startTimers() {
        val brewState = transferableModel?.brew?.getCurrentState()
        if (brewState?.brewTime != null) {
            startTimerStates(brewState, brewState.brewTime.inMilliseconds(), false)
        } else {
            state = TimerState.ErrorState("Something went wrong!")
        }
    }
}

interface ITimerViewModel {
    fun updateState(state: TimerState)
}

interface TimerIntents : MVIBaseView {
    fun init(transferableModel: TimerTransferableModel)
    fun startBrew()
    fun resume()
    fun exitTimer()
    fun updateTimer()
}

sealed class TimerState {
    object InitialState : TimerState()
    data class ErrorState(val data: String) : TimerState()
    data class UpdateDescriptionState(val brewState: BrewState, val animateDescription: Boolean) : TimerState()
    data class UpdateProgressBar(val maxValue: Int, val timeInMilliseconds: Long, val animate: Boolean, val update: Boolean) : TimerState()
    object PlaySoundState : TimerState()
    object ExitState : TimerState()
    object FinishState : TimerState()
}

interface TimerStateHandler {
    fun handleInitialState()
    fun handleErrorState(state: TimerState.ErrorState)
    fun handleUpdateDescriptionState(state: TimerState.UpdateDescriptionState)
    fun handleUpdateProgressBar(state: TimerState.UpdateProgressBar)
    fun handleExitState()
    fun handleFinishState()
    fun handlePlaySoundState()
}
