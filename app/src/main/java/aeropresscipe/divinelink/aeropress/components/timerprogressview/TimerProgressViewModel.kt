package aeropresscipe.divinelink.aeropress.components.timerprogressview

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.components.saverecipecard.ISaveRecipeCardViewModel
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardViewModel
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.getBrewingStates
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import aeropresscipe.divinelink.aeropress.timer.TimerState
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
    private var repository: TimerRepository,
    @Assisted override var delegate: WeakReference<ITimerProgressViewModel>? = null
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
        val bloomTimeLeft = dice?.bloomEndTimeMillis!! - System.currentTimeMillis()
        val brewTimeLeft = dice?.brewEndTimeMillis!! - System.currentTimeMillis()
        val bloomState = states?.find { state -> state.phase == Phase.Bloom }

        Timber.d("bloom time left: $bloomTimeLeft")
        Timber.d("brew time left: $brewTimeLeft")

        if (bloomTimeLeft > 0) {
            startTimerStates(bloomState!!, bloomTimeLeft)
        } else if (brewTimeLeft > 0) {
            // Remove bloomState from list and get BrewState for new timer.
            states?.remove(bloomState)
            val brewState = states?.find { state -> state.phase == Phase.Brew }
            startTimerStates(brewState!!, brewTimeLeft)
        } else {
            state = TimerProgressState.FinishState
        }

    }

    override fun updateTimer() {
        brew?.removeCurrentPhase()
        brew?.let { brew ->
            // Update current brew state
            if (brew.getCurrentState() == BrewState.Finished) {
                state = TimerProgressState.StartTimer(brewState = BrewState.Finished, true)
            } else {
                startTimerStates(brew.getCurrentState(), brew.getCurrentState().brewTime.inMilliseconds(), animate = true)
            }
//            if (!preferences.muteSound) {
//                state = TimerState.PlaySoundState
//            }
        }
    }

    private fun startTimerStates(brewState: BrewState, timeLeft: Long, animate: Boolean = false) {
        Timber.d("Update brewState: $brewState")
        Timber.d("Title: ${brewState.title}")
        Timber.d("Description: ${brewState.description}")
        state = TimerProgressState.StartTimer(brewState, animate)
        state = TimerProgressState.StartProgressBar(
            maxValue = brewState.brewTime.inMilliseconds().toInt(),
            timeInMilliseconds = timeLeft,
            animate = animate
        )
    }
}

interface ITimerProgressViewModel {
    fun updateState(state: TimerProgressState)
}

interface TimerProgressIntents : MVIBaseView {
    fun init(dice: DiceDomain?)
    fun getView()
    fun updateTimer()
}

sealed class TimerProgressState {
    object InitialState : TimerProgressState()
    data class StartTimer(val brewState: BrewState, val animate: Boolean) : TimerProgressState()
    data class StartProgressBar(val maxValue: Int, val timeInMilliseconds: Long, val animate: Boolean) : TimerProgressState()
    object RetryState : TimerProgressState()
    object FinishState : TimerProgressState()

}

interface TimerProgressStateHandler {
    fun handleInitialState()
    fun handleStartTimer(state: TimerProgressState.StartTimer)
    fun handleStartProgressBar(state: TimerProgressState.StartProgressBar)
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

@AssistedFactory
interface SaveRecipeCardViewModelAssistedFactory {
    fun create(delegate: WeakReference<ISaveRecipeCardViewModel>?): SaveRecipeCardViewModel
}
