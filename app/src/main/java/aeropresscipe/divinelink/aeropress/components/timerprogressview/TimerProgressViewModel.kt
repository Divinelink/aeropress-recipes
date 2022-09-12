package aeropresscipe.divinelink.aeropress.components.timerprogressview

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.getBrewTimeLeft
import aeropresscipe.divinelink.aeropress.generaterecipe.models.getBrewingStates
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.util.BrewPhase
import aeropresscipe.divinelink.aeropress.timer.util.BrewState
import aeropresscipe.divinelink.aeropress.timer.util.Phase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import gr.divinelink.core.util.extensions.inMilliseconds
import timber.log.Timber
import java.lang.ref.WeakReference

class TimerProgressViewModel @AssistedInject constructor(
    @Suppress("UnusedPrivateMember") private var repository: TimerRepository,
    @Assisted public override var delegate: WeakReference<ITimerProgressViewModel>? = null
) : BaseViewModel<ITimerProgressViewModel>(),
    TimerProgressIntents {
    internal var statesList: MutableList<TimerProgressState> = mutableListOf()

    private var dice: DiceDomain? = null
    private var brew: BrewPhase? = null

    var state: TimerProgressState = TimerProgressState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
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

interface TimerProgressIntents : MVIBaseView {
    fun init(dice: DiceDomain?)
    fun getView()
    fun updateTimer(update: Boolean)
}

sealed class TimerProgressState {
    object InitialState : TimerProgressState()
    data class UpdateDescriptionState(val brewState: BrewState) : TimerProgressState()
    data class UpdateProgressBar(val maxValue: Int, val timeInMilliseconds: Long, val animate: Boolean, val update: Boolean) : TimerProgressState()
    object RetryState : TimerProgressState()
    object FinishState : TimerProgressState()
}

interface TimerProgressStateHandler {
    fun handleInitialState()
    fun handleUpdateDescriptionState(state: TimerProgressState.UpdateDescriptionState)
    fun handleUpdateProgressBar(state: TimerProgressState.UpdateProgressBar)
    fun handleRetryState()
    fun handleFinishState()
}

@Suppress("UNCHECKED_CAST")
class TimerProgressViewModelFactory(
    private val assistedFactory: TimerProgressViewModelAssistedFactory,
    private val delegate: WeakReference<ITimerProgressViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerProgressViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface TimerProgressViewModelAssistedFactory {
    fun create(delegate: WeakReference<ITimerProgressViewModel>?): TimerProgressViewModel
}
