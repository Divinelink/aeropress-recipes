package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.Notification.Companion.make
import aeropresscipe.divinelink.aeropress.databinding.FragmentTimerBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import aeropresscipe.divinelink.aeropress.timer.util.TimerViewModelAssistedFactory
import aeropresscipe.divinelink.aeropress.timer.util.TimerViewModelFactory
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
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

@AndroidEntryPoint
class TimerFragment : Fragment(),
    ITimerViewModel,
    TimerStateHandler {
    private var binding: FragmentTimerBinding? = null

    @Inject
    lateinit var assistedFactory: TimerViewModelAssistedFactory
    private lateinit var viewModel: TimerViewModel

    private var transferableModel = TimerTransferableModel()

    private lateinit var timer: PreciseCountdown
    private var milliSecondsLeft = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        val view = binding?.root

        transferableModel.recipe = arguments?.getSerializable(TIMER) as Recipe?

        val viewModelFactory = TimerViewModelFactory(assistedFactory, WeakReference<ITimerViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimerViewModel::class.java)

        viewModel.init(transferableModel)

        viewModel.startBrew(transferableModel)

        initListeners()
        return view
    }

    override fun onPause() {
        // Use OnPause instead of OnStop, because onStop is called after we go back to HomeActivity,
        // and in this case we don't get the isBrewing boolean in brewTime
        super.onPause()
//        diceUI?.isNewRecipe = false
//        if (GetPhaseFactory().findPhase(diceUI.bloomTime, diceUI.brewTime ?: 0).brewTime != 0) {
//            val isBloomPhase = GetPhaseFactory().findPhase(diceUI.bloomTime ?: 0, diceUI.brewTime ?: 0).phases
//            timerHandler.removeCallbacks(runnable)
//            timerHandler.removeCallbacks(brewRunnable)
//            presenter?.saveValuesOnPause(context, milliSecondsLeft, diceUI.brewTime ?: 0, isBloomPhase)
//        } else {
//            // When leaving Timer and it is over, set isBrewing boolean to false, meaning that brewing process is over
//            // which removes the resume button on Generate Recipe Fragment.
//            BaseApplication.sharedPreferences.isBrewing = false
//        }
    }

    override fun onResume() {
        super.onResume()
//         if resuming from recipe without bloom isNewRecipe == false
//        if (diceUI?.isNewRecipe == true) { // if it's a new recipe, dont call returnValuesOnResume
//            presenter?.startBrewing(
//                GetPhaseFactory().findPhase(diceUI.bloomTime, diceUI?.brewTime).brewTime,
//                GetPhaseFactory().findPhase(diceUI.bloomTime, diceUI?.brewTime).phases,
//                context)
//        } else {
//            When resuming, we need to pass the old recipe, not the new one.
//            presenter?.returnValuesOnResume(context)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        binding?.likeRecipeButton?.setOnClickListener { viewModel.saveRecipe(transferableModel.recipe) }
        binding?.likeRecipeButton?.setOnTouchListener { view: View?, motionEvent: MotionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val reducer = AnimatorInflater.loadAnimator(context, R.animator.reduce_size) as AnimatorSet
                reducer.setTarget(view)
                reducer.start()
            } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                val regainer = AnimatorInflater.loadAnimator(context, R.animator.regain_size) as AnimatorSet
                regainer.setTarget(view)
                regainer.start()
            }
            false
        }
    }

    companion object {
        private const val INTERVAL = 10L
        const val TIMER = "TIMER"

        @JvmStatic
        fun newInstance(recipe: Recipe?): TimerFragment {
            val fragment = TimerFragment()
            val args = Bundle()
            args.putSerializable(TIMER, recipe)
            fragment.arguments = args
            return fragment
        }
    }

    override fun updateState(state: TimerState) {
        when (state) {
            is TimerState.InitialState -> handleInitialState()
            is TimerState.LoadingState -> handleLoadingState()
            is TimerState.ErrorState -> handleErrorState(state)

            is TimerState.RecipeRemovedState -> handleRecipeRemovedState()
            is TimerState.RecipeSavedState -> handleRecipeSavedState()

            is TimerState.UpdateSavedIndicator -> handleUpdateSavedIndicator(state)

            is TimerState.StartProgressBar -> handleStartProgressBar(state)
            is TimerState.StartTimer -> handleStartTimer(state)
            is TimerState.FinishState -> handleFinishState()
        }
    }

    override fun handleInitialState() {
//        TODO("Not yet implemented")
    }

    override fun handleLoadingState() {
//        TODO("Not yet implemented")
    }

    override fun handleErrorState(state: TimerState.ErrorState) {
//        TODO("Not yet implemented")
    }

    override fun handleRecipeSavedState() {
        make(binding?.likeRecipeButton, resources.getString(R.string.save_recipe_notification, getString(R.string.favourites))).show()
        binding?.likeRecipeButton?.setImageResource(R.drawable.ic_heart_on)
    }

    override fun handleRecipeRemovedState() {
        make(binding?.likeRecipeButton, resources.getString(R.string.remove_recipe_notification, getString(R.string.favourites))).show()
        binding?.likeRecipeButton?.setImageResource(R.drawable.ic_heart_off)
    }

    override fun handleStartTimer(state: TimerState.StartTimer) {
        binding?.timerHeader?.text = resources.getString(state.title)
        binding?.waterDescription?.text = resources.getString(state.description, state.water)
    }

    override fun handleStartProgressBar(state: TimerState.StartProgressBar) {
        binding?.progressBar?.max = state.timeInMilliseconds.toInt()
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
        timer.start()
    }

    override fun handleFinishState() {
        timer.dispose()
    }

    override fun handleUpdateSavedIndicator(state: TimerState.UpdateSavedIndicator) {
        binding?.likeRecipeButton?.setImageResource(state.image)
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
