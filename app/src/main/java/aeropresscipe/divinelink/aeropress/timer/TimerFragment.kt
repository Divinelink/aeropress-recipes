package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.finish.FinishActivity
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import android.animation.ObjectAnimator
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.divinelink.aeropress.recipes.R
import com.divinelink.aeropress.recipes.databinding.FragmentTimerBinding
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.constants.Numbers.ONE
import gr.divinelink.core.util.constants.Numbers.ONE_THOUSAND
import gr.divinelink.core.util.constants.Numbers.SIXTY
import gr.divinelink.core.util.constants.Numbers.THREE_HUNDRED
import gr.divinelink.core.util.extensions.getBundleSerializable
import gr.divinelink.core.util.extensions.getPairOfMinutesSeconds
import gr.divinelink.core.util.extensions.updateTextWithFade
import gr.divinelink.core.util.timer.PreciseCountdown
import java.lang.ref.WeakReference
import java.util.Locale

enum class TimerFlow {
  START,
  RESUME
}

@AndroidEntryPoint
class TimerFragment :
  Fragment(),
  ITimerViewModel,
  TimerStateHandler {
  private var binding: FragmentTimerBinding? = null

  private val viewModel: TimerViewModel by viewModels()
  private lateinit var callback: Callback

  private var transferableModel = TimerTransferableModel()

  private var timer: PreciseCountdown? = null
  private var mediaPlayer: MediaPlayer? = null

  private var milliSecondsLeft = 0L

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    binding = FragmentTimerBinding.inflate(inflater, container, false)
    val view = binding?.root

    viewModel.delegate = WeakReference(this)

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    transferableModel.recipe = arguments?.getBundleSerializable(TIMER)

    val flow = arguments?.getBundleSerializable<TimerFlow>(FLOW)
    viewModel.init(transferableModel)

    when (flow) {
      TimerFlow.START -> viewModel.startBrew()
      TimerFlow.RESUME -> viewModel.resume()
      else -> throw AssertionError("Flow could not be determined.")
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    timer?.dispose()
    mediaPlayer?.release()
    binding = null
  }

  companion object {
    private const val INTERVAL = 10L
    const val TIMER = "TIMER"
    const val FLOW = "FLOW"

    @JvmStatic
    fun newInstance(recipe: Recipe?, flow: TimerFlow? = TimerFlow.START): TimerFragment {
      val fragment = TimerFragment()
      val args = Bundle()
      args.putSerializable(TIMER, recipe)
      args.putSerializable(FLOW, flow)
      fragment.arguments = args
      return fragment
    }
  }

  override fun updateState(state: TimerState) {
    when (state) {
      is TimerState.InitialState -> handleInitialState()
      is TimerState.ErrorState -> handleErrorState(state)
      is TimerState.UpdateProgressBar -> handleUpdateProgressBar(state)
      is TimerState.UpdateDescriptionState -> handleUpdateDescriptionState(state)
      is TimerState.FinishState -> handleFinishState()
      is TimerState.ExitState -> handleExitState()
      is TimerState.PlaySoundState -> handlePlaySoundState()
    }
  }

  override fun handleExitState() {
    callback.onExitTimer()
  }

  override fun handleInitialState() {
    binding?.likeButtonCard?.recipe = transferableModel.recipe
    mediaPlayer = MediaPlayer.create(context, R.raw.timer_beep)
  }

  override fun handleErrorState(state: TimerState.ErrorState) {
    // Do nothing yet.
  }

  override fun handleUpdateDescriptionState(state: TimerState.UpdateDescriptionState) {
    if (state.animateDescription) {
      binding?.timerHeader updateTextWithFade resources.getString(state.brewState.title)
      binding?.waterDescription updateTextWithFade resources.getString(
        state.brewState.description,
        state.brewState.phaseWater,
        state.brewState.totalWater,
      )
    } else {
      binding?.timerHeader?.text = resources.getString(state.brewState.title)
      binding?.waterDescription?.text = resources.getString(
        state.brewState.description,
        state.brewState.phaseWater,
        state.brewState.totalWater,
      )
    }
  }

  override fun handleUpdateProgressBar(state: TimerState.UpdateProgressBar) {
    binding?.progressBar?.max = state.maxValue
    milliSecondsLeft = state.timeInMilliseconds

    if (state.animate) {
      ObjectAnimator
        .ofInt(
          binding?.progressBar, "progress",
          state.timeInMilliseconds.toInt(),
        )
        .setDuration(THREE_HUNDRED.toLong())
        .start()
    }

    timer = PreciseCountdown(
      totalTime = milliSecondsLeft,
      interval = INTERVAL,
      onTick = {
            updateCountdownUI(it)
        },
      onFinish = {
            viewModel.updateTimer()
        },
    )
    timer?.start()
  }

  override fun handleFinishState() {
    timer?.dispose()
    startActivity(FinishActivity.newIntent(requireContext(), transferableModel.recipe))
    callback.onExitTimer()
  }

  override fun handlePlaySoundState() {
    mediaPlayer?.start()
  }

  private fun updateCountdownUI(
    timeInMilliseconds: Long,
  ) {
    milliSecondsLeft -= INTERVAL
    val time = timeInMilliseconds / ONE_THOUSAND
    val timeLeft = time.getPairOfMinutesSeconds()
    val seconds: Long
    val minutes: Long
    if (timeLeft.second + ONE == SIXTY.toLong()) {
      minutes = timeLeft.first + ONE
      seconds = 0L
    } else {
      minutes = timeLeft.first
      seconds = timeLeft.second + ONE
    }
    binding?.brewingTimeTextView?.text = String.format(Locale.US, "%d:%02d", minutes, seconds)
    binding?.progressBar?.progress = timeInMilliseconds.toInt()
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    callback = context as Callback
  }

  interface Callback {
    fun onExitTimer()
  }
}
