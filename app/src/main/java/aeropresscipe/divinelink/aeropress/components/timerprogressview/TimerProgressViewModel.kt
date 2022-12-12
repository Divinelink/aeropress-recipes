package aeropresscipe.divinelink.aeropress.components.timerprogressview

import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.getBrewTimeLeft
import aeropresscipe.divinelink.aeropress.recipe.models.getBrewingStates
import aeropresscipe.divinelink.aeropress.timer.util.BrewPhase
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.Phase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.divinelink.core.util.extensions.inMilliseconds
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class TimerProgressViewModel @Inject constructor() :
    ViewModel(),
    TimerProgressIntents {
    internal var statesList: MutableList<TimerProgressState> = mutableListOf()
    var delegate: WeakReference<ITimerProgressViewModel>? = null

    private var dice: DiceDomain? = null
    private var brew: BrewPhase? = null

    var state: TimerProgressState = TimerProgressState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    override fun init(dice: DiceDomain?) {
        Timber.d("dice id: ${dice?.id}")
        this.dice = dice
        this.brew = BrewPhase.Builder()
            .brewStates(dice?.recipe?.getBrewingStates())
            .build()
        val states = brew?.brewStates

        state = if (states == null) {
            TimerProgressState.RetryState
        } else {
            TimerProgressState.InitialState
        }
    }

    override fun getView() {
        val states = brew?.brewStates
        val timeLeft: Pair<Long, Long> = dice.getBrewTimeLeft()
        val bloomState = states?.find { state -> state.phase == Phase.Bloom }

        Timber.d("bloom time left: ${timeLeft.first}")
        Timber.d("brew time left: ${timeLeft.second}")

        if (timeLeft.first > 0) {
            startTimerStates(bloomState, timeLeft.first)
        } else if (timeLeft.second > 0) {
            // Remove bloomState from list and get BrewState for new timer.
            states?.remove(bloomState)
            val brewState = states?.find { state -> state.phase == Phase.Brew }
            startTimerStates(brewState, timeLeft.second)
        } else {
            state = TimerProgressState.UpdateDescriptionState(BrewState.Finished)
        }
    }

    override fun updateTimer(update: Boolean) {
        if (update) {
            brew?.removeCurrentPhase()
            val state = brew?.getCurrentState()
            startTimerStates(state, state?.brewTime.inMilliseconds())
            //            if (!preferences.muteSound) {
            //                state = TimerState.PlaySoundState
            //            }
        }
    }

    private fun startTimerStates(brewState: BrewState?, timeLeft: Long) {
        if (brewState == null) return
        Timber.d("Update brewState: $brewState")
        Timber.d("Title: ${brewState.title}")
        Timber.d("Description: ${brewState.description}")
        state = TimerProgressState.UpdateDescriptionState(brewState)
        state = TimerProgressState.UpdateProgressBar(
            maxValue = brewState.brewTime.inMilliseconds().toInt(),
            timeInMilliseconds = timeLeft,
            animate = brewState.animate,
            update = brewState.update
        )
    }
}

interface ITimerProgressViewModel {
    fun updateState(state: TimerProgressState)
}

interface TimerProgressIntents {
    fun init(dice: DiceDomain?)
    fun getView()
    fun updateTimer(update: Boolean)
}

sealed class TimerProgressState {
    object InitialState : TimerProgressState()
    data class UpdateDescriptionState(val brewState: BrewState) : TimerProgressState()
    data class UpdateProgressBar(
        val maxValue: Int,
        val timeInMilliseconds: Long,
        val animate: Boolean,
        val update: Boolean,
    ) : TimerProgressState()

    object RetryState : TimerProgressState()
}

interface TimerProgressStateHandler {
    fun handleInitialState()
    fun handleUpdateDescriptionState(state: TimerProgressState.UpdateDescriptionState)
    fun handleUpdateProgressBar(state: TimerProgressState.UpdateProgressBar)
    fun handleRetryState()
}
