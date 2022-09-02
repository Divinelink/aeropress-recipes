package aeropresscipe.divinelink.aeropress.home

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class HomeViewModel @AssistedInject constructor(
    var repository: HomeRepository,
    @Assisted override var delegate: WeakReference<IHomeViewModel>? = null
) : BaseViewModel<IHomeViewModel>(),
    HomeIntents {
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
        getResumeState()
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

interface HomeIntents : MVIBaseView {
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

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val assistedFactory: HomeViewModelAssistedFactory,
    private val delegate: WeakReference<IHomeViewModel>?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return assistedFactory.create(delegate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@AssistedFactory
interface HomeViewModelAssistedFactory {
    fun create(delegate: WeakReference<IHomeViewModel>?): HomeViewModel
}
