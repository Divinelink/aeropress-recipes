package aeropresscipe.divinelink.aeropress.timer

import aeropresscipe.divinelink.aeropress.customviews.Notification.Companion.make
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentTimerBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
import aeropresscipe.divinelink.aeropress.timer.util.TimerTransferableModel
import aeropresscipe.divinelink.aeropress.timer.util.TimerViewModelFactory
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.animation.AnimatorSet
import android.animation.AnimatorInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import gr.divinelink.core.util.timer.Timer
import java.lang.ref.WeakReference


class TimerFragment : Fragment(),
    ITimerViewModel,
    TimerStateHandler {
    private var binding: FragmentTimerBinding? = null

    private lateinit var viewModel: TimerViewModel
    private lateinit var viewModelFactory: TimerViewModelFactory

    private var transferableModel = TimerTransferableModel()

    private var millisecondsRemaining = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        val view = binding?.root

        transferableModel.recipe = arguments?.getSerializable("timer") as Recipe

        viewModelFactory = TimerViewModelFactory(
            app = requireActivity().application,
            delegate = WeakReference<ITimerViewModel>(this),
            repository = TimerRepository(),
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(TimerViewModel::class.java)

        viewModel.init(transferableModel)

        viewModel.startBrew(transferableModel)

        initListeners()
        return view
    }

//    override fun showTimer(time: Int, bloomPhase: Boolean) {
//        millisecondsRemaining = time
//        if (bloomPhase) {
//            timerHandler.postDelayed(runnable, 10)
//            updateCountdownUI()
//            binding?.timerHeader?.text = String.format("%s\n%s", getString(R.string.bloomPhase), getString(R.string.bloomPhaseWaterText, diceUI?.bloomWater))
//            diceUI?.setRecipeHadBloom(true)
//        } else {
//            timerHandler.postDelayed(brewRunnable, 10)
//            // Checks if there was a bloom or not, and set corresponding text on textView.
//            updateCountdownUI()
//            if (GetPhaseFactory().findPhase(diceUI?.bloomTime ?: 0, diceUI?.brewTime ?: 0).phases || diceUI?.recipeHadBloom() == true) {
//                binding?.timerHeader?.text = String.format("%s\n%s", getString(R.string.brewPhase), getString(R.string.brewPhaseWithBloom, diceUI?.remainingBrewWater))
//            } else {
//                binding?.timerHeader?.text = String.format("%s\n%s", getString(R.string.brewPhase), getString(R.string.brewPhaseNoBloom, diceUI?.remainingBrewWater))
//            }
//            diceUI.bloomTime = 0
//        }
//        // Set max progress bar to be either the max BloomTime or BrewTime.
//        // Avoid if statements by using Factory
//        binding?.progressBar?.max = GetPhaseFactory().getMaxTime(diceUI.bloomTime, diceUI.brewTime) * 1000
//
////        ObjectAnimator.ofInt(binding?.progressBar, "progress", brewTime)
////                .setDuration(300)
////                .start();
//    }


//    private var brewRunnable: Runnable = object : Runnable {
//        override fun run() {
//            if (millisecondsRemaining < 10) {
//                timerHandler.removeCallbacks(this)
//                //TODO ADD ANIMATION
////                presenter?.showMessage()
//            } else {
//                timerHandler.postDelayed(this, 10)
//                millisecondsRemaining -= 10
////                updateCountdownUI()
//            }
//        }
//    }

    override fun onPause() {
        // Use OnPause instead of OnStop, because onStop is called after we go back to HomeActivity,
        // and in this case we don't get the isBrewing boolean in brewTime
        super.onPause()
//        diceUI?.isNewRecipe = false
//        if (GetPhaseFactory().findPhase(diceUI.bloomTime, diceUI.brewTime ?: 0).brewTime != 0) {
//            val isBloomPhase = GetPhaseFactory().findPhase(diceUI.bloomTime ?: 0, diceUI.brewTime ?: 0).phases
//            timerHandler.removeCallbacks(runnable)
//            timerHandler.removeCallbacks(brewRunnable)
//            presenter?.saveValuesOnPause(context, millisecondsRemaining, diceUI.brewTime ?: 0, isBloomPhase)
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
        private const val REDUCE_RATE = 10L

        @JvmStatic
        fun newInstance(dice: Recipe?): TimerFragment {
            val fragment = TimerFragment()
            val args = Bundle()
            args.putSerializable("timer", dice)
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
        millisecondsRemaining = state.timeInMilliseconds

        if (state.animate) {
            ObjectAnimator
                .ofInt(
                    binding?.progressBar, "progress",
                    state.timeInMilliseconds.toInt()
                )
                .setDuration(300)
                .start()
        }

        Timer(
            millisInFuture = millisecondsRemaining,
            countDownInterval = REDUCE_RATE,
            runAtStart = true,
            onFinish = { viewModel.updateTimer() },
            onTick = {
                updateCountdownUI(millisecondsRemaining)
            }
        )
    }

    override fun handleFinishState() {
        // to do
    }

    override fun handleUpdateSavedIndicator(state: TimerState.UpdateSavedIndicator) {
        binding?.likeRecipeButton?.setImageResource(state.image)
    }

    private fun updateCountdownUI(
        timeInMilliseconds: Long,
    ) {
        val time = timeInMilliseconds / 1000
        val timeLeft = time.getPairOfMinutesSeconds()
        binding?.brewingTimeTextView?.text = String.format("%d:%02d", timeLeft.first, timeLeft.second)
        binding?.progressBar?.progress = timeInMilliseconds.toInt()
        millisecondsRemaining -= REDUCE_RATE
    }


}