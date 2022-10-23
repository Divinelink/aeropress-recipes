package aeropresscipe.divinelink.aeropress.home

import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import aeropresscipe.divinelink.aeropress.ui.theme.ThemedActivityDelegate
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    var repository: HomeRepository,
    themedActivityDelegate: ThemedActivityDelegate,
) : ViewModel(),
    HomeIntents,
    ThemedActivityDelegate by themedActivityDelegate {
    var delegate: WeakReference<IHomeViewModel>? = null
    internal var statesList: MutableList<HomeState> = mutableListOf()

    private lateinit var dice: DiceDomain
    private var canResume = false

    var state: HomeState = HomeState.InitialState
        set(value) {
            Timber.d(value.toString())
            field = value
            delegate?.get()?.updateState(value)
        }

    override fun init() {
        state = HomeState.InitialState
    }

    override fun resume() {
        getResumeState()
    }

    private fun getResumeState() {
        repository.getRecipe { dice ->
            this.dice = dice
            state = when (dice.isBrewing) {
                true -> HomeState.ShowResumeButtonState(dice)
                false -> HomeState.HideResumeButtonState
            }
            canResume = dice.isBrewing
        }
    }

    override fun generateRecipe() {
        state = HomeState.HideResumeButtonState
        canResume = false
    }

    override fun startTimer(recipe: Recipe, flow: TimerFlow, update: Boolean) {
        repository.updateRecipe(recipe, update) { dice ->
            Timber.d("Starting timer with recipeId: ${dice.id}")
            this.dice = dice
            state = HomeState.StartTimerState(dice.recipe, flow)
        }
    }

    override fun resumeTimer() {
        if (canResume) {
            state = HomeState.StartTimerState(dice.recipe, TimerFlow.RESUME)
        }
    }
}

interface IHomeViewModel {
    fun updateState(state: HomeState)
}

interface HomeIntents {
    fun init()
    fun resume()
    fun resumeTimer()
    fun generateRecipe()
    fun startTimer(recipe: Recipe, flow: TimerFlow, update: Boolean)
}

sealed class HomeState {
    object InitialState : HomeState()
    data class ShowResumeButtonState(val dice: DiceDomain) : HomeState()
    object HideResumeButtonState : HomeState()
    data class StartTimerState(val recipe: Recipe, val flow: TimerFlow) : HomeState()
}

interface HomeStateHandler {
    fun handleInitialState()
    fun handleShowResumeButtonState(state: HomeState.ShowResumeButtonState)
    fun handleHideResumeButtonState()
    fun handleStartTimerState(state: HomeState.StartTimerState)
}
