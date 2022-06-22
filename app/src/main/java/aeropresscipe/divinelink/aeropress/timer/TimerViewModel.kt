package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.base.mvi.BaseAndroidViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
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

    var state: TimerState = TimerState.InitialState
        set(value) {
            Log.d(TimerViewModel::class.simpleName, state.javaClass.simpleName)
            field = value
            statesList.add(value)
            delegate?.get()?.updateState(value)
        }


    override fun startBrew(transferableModel: TimerTransferableModel?) {
        // Initialise Timer
        if (transferableModel?.dice == null) {
            state = TimerState.ErrorState("Something went wrong!") // Fixme
        } else {
            // Update History
            dbRepository.addToHistory(
                recipe = transferableModel.dice,
                brewDate = getCurrentDate(),
                context = getApplication(),
                completionBlock = {
                    if (transferableModel.bloomTime != 0L) {
                        state = TimerState.StartBloomTimerState(transferableModel.bloomTime)
                    } else if (transferableModel.brewTime != 0L) {
                        state = TimerState.StartBloomTimerState(transferableModel.brewTime)
                    }
                }
            )
        }
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
                    } else {
                        state = TimerState.RecipeRemovedState
                        dbRepository.updateHistory(recipe, getCurrentDate(), recipeLiked, getApplication()) { /* Intentionally Blank. */ }
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
    fun startBrew(transferableModel: TimerTransferableModel?)
    fun saveRecipe(recipe: DiceDomain?)
}

sealed class TimerState {
    object InitialState : TimerState()
    object LoadingState : TimerState()
    data class ErrorState(val data: String) : TimerState()

    object RecipeSavedState : TimerState()
    object RecipeRemovedState : TimerState()

    data class StartBloomTimerState(val bloomTime: Long) : TimerState()
    data class StartBrewTimerState(val brewTime: Long) : TimerState()
}

interface TimerStateHandler {
    fun handleInitialState()
    fun handleLoadingState()
    fun handleErrorState()

    fun handleSaveRecipeState()
    fun handleStartBloomTimerState(state: TimerState.StartBloomTimerState)
    fun handleStartBrewTimerState(state: TimerState.StartBrewTimerState)
}

