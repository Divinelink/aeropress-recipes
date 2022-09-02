package aeropresscipe.divinelink.aeropress.components.timerprogressview

import aeropresscipe.divinelink.aeropress.databinding.ViewTimerProgressBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.DiceDomain
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.fadeOut
import gr.divinelink.core.util.extensions.updateTextWithFade
import gr.divinelink.core.util.timer.PreciseCountdown
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class TimerProgressView : FrameLayout,
    ITimerProgressViewModel,
    TimerProgressStateHandler {
    var binding: ViewTimerProgressBinding = ViewTimerProgressBinding.inflate(LayoutInflater.from(context), this, false)

    @Inject
    lateinit var assistedFactory: TimerProgressViewModelAssistedFactory
    private lateinit var viewModel: TimerProgressViewModel

    private var milliSecondsLeft = 0L
    private var timer: PreciseCountdown? = null

    private var callback: Callback? = null

    var dice: DiceDomain? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val viewModelFactory = TimerProgressViewModelFactory(assistedFactory, WeakReference<ITimerProgressViewModel>(this))
        viewModel = ViewModelProvider(ViewTreeViewModelStoreOwner.get(this)!!, viewModelFactory)[TimerProgressViewModel::class.java]
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addView(binding.root)
    }

    override fun updateState(state: TimerProgressState) {
        when (state) {
            is TimerProgressState.InitialState -> handleInitialState()
            is TimerProgressState.RetryState -> handleRetryState()
            is TimerProgressState.FinishState -> handleFinishState()
            is TimerProgressState.StartProgressBar -> handleStartProgressBar(state)
            is TimerProgressState.StartTimer -> handleStartTimer(state)
        }
    }

    override fun handleInitialState() {
        viewModel.getView()
    }

    override fun handleRetryState() {
        dice?.let { viewModel.init(it) }
    }

    override fun handleStartTimer(state: TimerProgressState.StartTimer) {
        callback?.onTimerAttached()
//        if (state.animate) {
//            binding.brewStateTitle updateTextWithFade resources.getString(state.brewState.title)
//            binding.stateDescription updateTextWithFade resources.getString(state.brewState.description, state.brewState.brewWater)
//        } else {
        binding.brewStateTitle.text = resources.getString(state.brewState.title)
        binding.stateDescription.text = resources.getString(state.brewState.description, state.brewState.brewWater)
//        }
    }

    override fun handleStartProgressBar(state: TimerProgressState.StartProgressBar) {
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
                viewModel.updateTimer()
            }
        )
        timer?.start()
    }

    private fun updateCountdown(
        timeInMilliseconds: Long,
    ) {
        milliSecondsLeft -= INTERVAL
        binding.progressBar.progress = timeInMilliseconds.toInt()
    }

    override fun handleFinishState() {
        // Nothing yet
    }

    /**
     * Automatically shows root's view.
     */
    fun setTimerView(dice: DiceDomain, onViewAttached: (() -> Unit)) {
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
