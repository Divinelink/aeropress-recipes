package aeropresscipe.divinelink.aeropress.finish

import aeropresscipe.divinelink.aeropress.base.mvi.BaseViewModel
import aeropresscipe.divinelink.aeropress.base.mvi.MVIBaseView
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.ref.WeakReference

class FinishViewModel @AssistedInject constructor(
  @Suppress("UnusedPrivateMember") private var repository: TimerRepository,
  @Assisted public override var delegate: WeakReference<IFinishViewModel>? = null,
) : BaseViewModel<IFinishViewModel>(),
  FinishIntents {

  internal var statesList: MutableList<FinishState> = mutableListOf()

  private lateinit var recipe: Recipe

  var state: FinishState = FinishState.InitialState
    set(value) {
      Timber.d(value.toString())
      field = value
      delegate?.get()?.updateState(value)
      statesList.add(value)
    }

  override fun init(recipe: Recipe?) {
    state = FinishState.InitialState
    if (recipe != null) {
      state = FinishState.SetupRecipeState(recipe)
      this.recipe = recipe
    }
  }

  override fun closeButtonClicked() {
    state = FinishState.CloseState
  }
}

interface IFinishViewModel {
  fun updateState(state: FinishState)
}

interface FinishIntents : MVIBaseView {
  fun init(recipe: Recipe?)
  fun closeButtonClicked()
}

sealed class FinishState {
  object InitialState : FinishState()
  object CloseState : FinishState()
  data class SetupRecipeState(val recipe: Recipe) : FinishState()
}

interface FinishStateHandler {
  fun handleInitialState()
  fun handleCloseState()
  fun handleSetupRecipeState(state: FinishState.SetupRecipeState)
}
