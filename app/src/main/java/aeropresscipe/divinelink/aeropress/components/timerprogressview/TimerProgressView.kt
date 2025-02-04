package aeropresscipe.divinelink.aeropress.components.timerprogressview

import aeropresscipe.divinelink.aeropress.components.timerprogressview.TimerProgressView.Callback
import aeropresscipe.divinelink.aeropress.recipe.models.DiceDomain
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.divinelink.aeropress.recipes.databinding.ViewTimerProgressBinding
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.fadeOut
import gr.divinelink.core.util.timer.PreciseCountdown
import timber.log.Timber
import java.lang.ref.WeakReference

@AndroidEntryPoint
class TimerProgressView :
  FrameLayout,
  ITimerProgressViewModel,
  TimerProgressStateHandler {
  var binding: ViewTimerProgressBinding =
    ViewTimerProgressBinding.inflate(LayoutInflater.from(context), this, false)

  private lateinit var viewModel: TimerProgressViewModel

  private var milliSecondsLeft = 0L
  private var timer: PreciseCountdown? = null

  private var callback: Callback? = null

  var dice: DiceDomain? = null

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    viewModel =
      ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[TimerProgressViewModel::class.java]
    viewModel.delegate = WeakReference(this)
  }

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr,
  ) {
    addView(binding.root)
  }

  override fun updateState(state: TimerProgressState) {
    when (state) {
      is TimerProgressState.InitialState -> handleInitialState()
      is TimerProgressState.RetryState -> handleRetryState()
      is TimerProgressState.UpdateProgressBar -> handleUpdateProgressBar(state)
      is TimerProgressState.UpdateDescriptionState -> handleUpdateDescriptionState(state)
    }
  }

  override fun handleInitialState() {
    viewModel.getView()
  }

  override fun handleRetryState() {
    dice?.let { viewModel.init(it) }
  }

  override fun handleUpdateDescriptionState(state: TimerProgressState.UpdateDescriptionState) {
    callback?.onTimerAttached()
    binding.brewStateTitle.text = resources.getString(state.brewState.title)
    binding.stateDescription.text = resources.getString(
      state.brewState.description,
      state.brewState.phaseWater,
      state.brewState.totalWater,
    )
    binding.stateDescription.isSelected = true
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    timer?.dispose()
  }

  override fun handleUpdateProgressBar(state: TimerProgressState.UpdateProgressBar) {
    binding.progressBar.max = state.maxValue
    milliSecondsLeft = state.timeInMilliseconds

    if (state.animate) {
      ObjectAnimator
        .ofInt(binding.progressBar, "progress", state.timeInMilliseconds.toInt())
        .setDuration(REFILL_ANIMATION_DURATION)
        .start()
    }

    timer = PreciseCountdown(
      totalTime = milliSecondsLeft,
      interval = INTERVAL,
      onTick = {
        updateCountdown(it)
      },
      onFinish = {
        viewModel.updateTimer(state.update)
      },
    )
    timer?.start()
  }

  private fun updateCountdown(timeInMilliseconds: Long) {
    milliSecondsLeft -= INTERVAL
    binding.progressBar.progress = timeInMilliseconds.toInt()
  }

  /**
   * Automatically shows root's view.
   */
  fun setTimerView(
    dice: DiceDomain,
    onViewAttached: (() -> Unit),
  ) {
    Timber.d("Set Timer View")
    // Reset timer if already initialised
    timer?.dispose()
    milliSecondsLeft = 0L

    this.dice = dice
    this.callback = Callback {
      onViewAttached.invoke()
      show()
    }
    viewModel.init(dice)
  }

  /**
   * Automatically hides root's view.
   */
  fun dispose() {
    timer?.dispose()
    milliSecondsLeft = 0L
    dice = null
    hide()
  }

  private fun hide() {
    binding.root.fadeOut()
  }

  private fun show() {
    binding.root.visibility = View.VISIBLE
  }

  private fun interface Callback {
    fun onTimerAttached()
  }

  companion object {
    private const val REFILL_ANIMATION_DURATION = 300L
    private const val INTERVAL = 10L
  }
}
