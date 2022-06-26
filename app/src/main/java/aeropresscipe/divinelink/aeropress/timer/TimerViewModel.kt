package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.mvi.BaseAndroidViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import aeropresscipe.divinelink.aeropress.timer.util.BrewPhase
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date

class TimerViewModel(
    application: Application,
    override var delegate: WeakReference<ITimerViewModel>?,
    private var dbRepository: TimerRepository = TimerRepository(),
) : BaseAndroidViewModel<ITimerViewModel>(application),
    TimerIntents {
    internal var statesList: MutableList<TimerState> = mutableListOf()

    private var transferableModel: TimerTransferableModel? = null

    var state: TimerState = TimerState.InitialState
        set(value) {
            Log.d(TimerViewModel::class.simpleName, state.javaClass.simpleName)
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }

    override fun init(transferableModel: TimerTransferableModel) {
        this.transferableModel = transferableModel
        dbRepository.isRecipeSaved(transferableModel.dice, getApplication()) { saved ->
            val image = when (saved) {
                true -> R.drawable.ic_heart_on
                false -> R.drawable.ic_heart_off
            }
            state = TimerState.UpdateSavedIndicator(image)
        }
    }

    override fun startBrew(transferableModel: TimerTransferableModel?) {
        if (transferableModel?.dice == null) {
            state = TimerState.ErrorState("Something went wrong!")
        } else {
            // Initialise Timer
            dbRepository.addToHistory(
                recipe = transferableModel.dice!!,
                brewDate = getCurrentDate(),
                context = getApplication(),
                completionBlock = {
                    startTimers(transferableModel)
                }
            )
        }
    }

    override fun updateTimer(phase: Phase, transferableModel: TimerTransferableModel) {

    }

    private fun startTimers(transferableModel: TimerTransferableModel?) {
        val brewPhase = BrewPhase.Builder()
            .withCurrentPhase(getCorrectPhase())
            .brewTime(transferableModel?.dice?.brewTime?.toLong())
            .brewWater(transferableModel?.dice?.brewWaterAmount)
            .bloomTime(transferableModel?.dice?.bloomTime?.toLong())
            .bloomWater(transferableModel?.dice?.bloomWater)
            .withBloom(transferableModel?.dice?.withBloom)
            .remainingWater(transferableModel?.dice?.remainingWater)
            .build()

//        brewPhase.phases

        state = TimerState.StartTimer(
            water = brewPhase.water(),
            title = brewPhase.title(),
            description = brewPhase.description()
        )

        state = TimerState.StartProgressBar(
            timeInMilliseconds = brewPhase.time().inMilliseconds(),
            phase = brewPhase.currentPhase
        )
    }

    override fun saveRecipe(recipe: DiceDomain?) {
        if (recipe == null) {
            state = TimerState.ErrorState("Something went wrong!") // Fixme
        } else {
            // Save Recipe on DB
            val savedRecipe = SavedRecipeDomain(recipe, getCurrentDate())
            dbRepository.likeCurrentRecipe(
                recipe = savedRecipe,
                context = getApplication(),
                completionBlock = { recipeLiked ->
                    if (recipeLiked) {
                        state = TimerState.RecipeSavedState
                        dbRepository.updateHistory(recipe, getCurrentDate(), recipeLiked, getApplication()) { /* Intentionally Blank. */ }
//                        recipe.recipeSaved = true
                    } else {
                        state = TimerState.RecipeRemovedState
                        dbRepository.updateHistory(recipe, getCurrentDate(), recipeLiked, getApplication()) { /* Intentionally Blank. */ }
//                        recipe.recipeSaved = false
                    }
                }
            )


        }
    }

    private fun getCorrectPhase(): Phase {
        return if (transferableModel?.dice?.bloomTime == 0) {
            Phase.BREW
        } else {
            Phase.BLOOM
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd MMMM yyyy")
        return formatter.format(date.time)
    }

}

fun Long.inMilliseconds(): Long {
    return this * 1000
}

fun Long.getPairOfMinutesSeconds(): Pair<Long, Long> {
    val minutesUntilFinished = this / 60
    val secondsInMinuteUntilFinished = this - minutesUntilFinished * 60
    return Pair(minutesUntilFinished, secondsInMinuteUntilFinished)
}

interface ITimerViewModel {
    fun updateState(state: TimerState)
}

interface TimerIntents : MVIBaseView {
    fun init(transferableModel: TimerTransferableModel)
    fun startBrew(transferableModel: TimerTransferableModel?)
    fun saveRecipe(recipe: DiceDomain?)

    fun updateTimer(phase: Phase, transferableModel: TimerTransferableModel)
}

sealed class TimerState {
    object InitialState : TimerState()
    object LoadingState : TimerState()
    data class ErrorState(val data: String) : TimerState()

    object RecipeSavedState : TimerState()
    object RecipeRemovedState : TimerState()

    data class UpdateSavedIndicator(@DrawableRes val image: Int) : TimerState()

    data class StartTimer(val water: Int, @StringRes val title: Int, @StringRes val description: Int) : TimerState()
    data class StartProgressBar(val timeInMilliseconds: Long, val phase: Phase) : TimerState()
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
}

enum class Phase {
    BLOOM,
    BREW,
}
