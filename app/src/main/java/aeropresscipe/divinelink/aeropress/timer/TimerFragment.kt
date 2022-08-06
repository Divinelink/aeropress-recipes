package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.databinding.FragmentTimerBinding
import aeropresscipe.divinelink.aeropress.finish.FinishActivity
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import aeropresscipe.divinelink.aeropress.timer.util.TimerViewModelAssistedFactory
import aeropresscipe.divinelink.aeropress.timer.util.TimerViewModelFactory
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.constants.Numbers.ONE
import gr.divinelink.core.util.constants.Numbers.ONE_THOUSAND
import gr.divinelink.core.util.constants.Numbers.SIXTY
import gr.divinelink.core.util.constants.Numbers.THREE_HUNDRED
import gr.divinelink.core.util.extensions.getPairOfMinutesSeconds
import gr.divinelink.core.util.timer.PreciseCountdown
import java.lang.ref.WeakReference
import java.util.Locale
import javax.inject.Inject

enum class TimerFlow {
    START,
    RESUME
}

@AndroidEntryPoint
class TimerFragment : Fragment(),
    ITimerViewModel,
    TimerStateHandler {
    private var binding: FragmentTimerBinding? = null

    @Inject
    lateinit var assistedFactory: TimerViewModelAssistedFactory
    private lateinit var viewModel: TimerViewModel

    private var transferableModel = TimerTransferableModel()

    private var timer: PreciseCountdown? = null
    private var milliSecondsLeft = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        val view = binding?.root

        transferableModel.recipe = arguments?.getSerializable(TIMER) as Recipe?
        val flow = arguments?.getSerializable(FLOW) as TimerFlow

        val viewModelFactory = TimerViewModelFactory(assistedFactory, WeakReference<ITimerViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimerViewModel::class.java)

        viewModel.init(transferableModel)

        when (flow) {
            TimerFlow.START -> viewModel.startBrew()
            TimerFlow.RESUME -> viewModel.resume()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.dispose()
        viewModel.exitTimer(milliSecondsLeft)
        binding = null
    }

    companion object {
        private const val INTERVAL = 10L
        const val TIMER = "TIMER"
        const val FLOW = "FLOW"

        const val LIKE_MIN_FRAME = 0
        const val LIKE_MAX_FRAME = 80

        const val DISLIKE_MIN_FRAME = 80
        const val DISLIKE_MAX_FRAME = 130

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
            is TimerState.StartProgressBar -> handleStartProgressBar(state)
            is TimerState.StartTimer -> handleStartTimer(state)
            is TimerState.FinishState -> handleFinishState()
            is TimerState.ExitState -> handleExitState()
        }
    }

    override fun handleExitState() {
        // Do nothing yet.
    }

    override fun handleInitialState() {
        binding?.likeButtonCard?.recipe = transferableModel.recipe
    }

    override fun handleErrorState(state: TimerState.ErrorState) {
        // Do nothing yet.
    }

    override fun handleStartTimer(state: TimerState.StartTimer) {
        binding?.timerHeader?.text = resources.getString(state.title)
        binding?.waterDescription?.text = resources.getString(state.description, state.water)
    }

    override fun handleStartProgressBar(state: TimerState.StartProgressBar) {
        binding?.progressBar?.max = state.maxValue
        milliSecondsLeft = state.timeInMilliseconds

        if (state.animate) {
            ObjectAnimator
                .ofInt(
                    binding?.progressBar, "progress",
                    state.timeInMilliseconds.toInt()
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
            }
        )
        timer?.start()
    }

    override fun handleFinishState() {
        timer?.dispose()
        startActivity(FinishActivity.newIntent(requireContext(), transferableModel.recipe))
        activity?.finish()
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
}
