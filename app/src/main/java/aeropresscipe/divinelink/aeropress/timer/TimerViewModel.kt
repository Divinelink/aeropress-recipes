package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.getBrewingStates
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
    @Assisted override var delegate: WeakReference<ITimerViewModel>? = null,
) : BaseViewModel<ITimerViewModel>(),
    TimerIntents {
    internal var statesList: MutableList<TimerState> = mutableListOf()

    internal var transferableModel: TimerTransferableModel? = null

    var state: TimerState = TimerState.InitialState
        set(value) {
            Timber.d(state.javaClass.simpleName)
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    init {
        state = TimerState.InitialState
    }

    override fun init(transferableModel: TimerTransferableModel) {
        this.transferableModel = transferableModel
        val brewPhase = BrewPhase.Builder()
            .brewStates(transferableModel.recipe?.getBrewingStates())
            .build()
        this.transferableModel?.brew = brewPhase
    }

    override fun resume() {
        repository.resume {
            val states = transferableModel?.brew?.brewStates
            val bloomTimeLeft = it.bloomEndTimeMillis - System.currentTimeMillis()
            val brewTimeLeft = it.brewEndTimeMillis - System.currentTimeMillis()
            val bloomState = states?.find { state -> state.phase == Phase.Bloom }

            if (bloomTimeLeft > 0) {
                startTimerStates(bloomState!!, bloomTimeLeft)
            } else if (brewTimeLeft > 0) {
                // Remove bloomState from list and get BrewState for new timer.
                states?.remove(bloomState)
                val brewState = states?.find { state -> state.phase == Phase.Brew }
                startTimerStates(brewState!!, brewTimeLeft)
            } else {
                finishRecipeBrewing()
            }

            repository.addToHistory(it.recipe) {
                // Do nothing
            }
        }
    }

    private fun finishRecipeBrewing() {
        repository.updateTimes(bloomEnds = 0L, brewEnds = 0L)
        repository.updateBrewingState(false) {
            Timber.d("Recipe finished brewing")
            state = TimerState.FinishState
            state = TimerState.PlaySoundState
        }
    }

    private fun startTimerStates(brewState: BrewState, timeLeft: Long, animate: Boolean = false) {
        state = TimerState.StartTimer(brewState, animate)
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
            if (brew.getCurrentState() == BrewState.Finished) {
                finishRecipeBrewing()
            } else {
                startTimerStates(brew.getCurrentState(), brew.getCurrentState().brewTime.inMilliseconds(), animate = true)
            }
            state = TimerState.PlaySoundState
        }
    }

    override fun exitTimer(millisecondsLeft: Long) {
        val bloomEnds: Long
        val brewEnds: Long
        when (transferableModel?.brew?.getCurrentState()) {
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
                    state = TimerState.ExitState
                }
            }
        }
    }

    private fun startTimers() {
        val brewState = transferableModel?.brew?.getCurrentState()
        if (brewState?.brewTime != null) {
            startTimerStates(brewState, brewState.brewTime.inMilliseconds())
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
    fun exitTimer(millisecondsLeft: Long)
    fun updateTimer()
}

sealed class TimerState {
    object InitialState : TimerState()
    data class ErrorState(val data: String) : TimerState()
    data class StartTimer(val brewState: BrewState, val animate: Boolean) : TimerState()
    data class StartProgressBar(val maxValue: Int, val timeInMilliseconds: Long, val animate: Boolean) : TimerState()
    object PlaySoundState : TimerState()
    object ExitState : TimerState()
    object FinishState : TimerState()
}

interface TimerStateHandler {
    fun handleInitialState()
    fun handleErrorState(state: TimerState.ErrorState)
    fun handleStartTimer(state: TimerState.StartTimer)
    fun handleStartProgressBar(state: TimerState.StartProgressBar)
    fun handleExitState()
    fun handleFinishState()
    fun handlePlaySoundState()
}
