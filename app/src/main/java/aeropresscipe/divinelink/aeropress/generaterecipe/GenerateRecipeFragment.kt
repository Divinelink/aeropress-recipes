package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.Notification
import aeropresscipe.divinelink.aeropress.databinding.FragmentGenerateRecipeBinding
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.fadeOut
import gr.divinelink.core.util.utils.ThreadUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class GenerateRecipeFragment :
    Fragment(),
    IGenerateRecipeViewModel,
    GenerateRecipeStateHandler {
    private var binding: FragmentGenerateRecipeBinding? = null

    @Inject
    lateinit var assistedFactory: GenerateRecipeViewModelAssistedFactory
    private lateinit var viewModel: GenerateRecipeViewModel

    private lateinit var fadeInAnimation: Animation
    private lateinit var adapterAnimation: Animation

    private val currentMoment: Instant by lazy { Clock.System.now() }
    private val datetime: LocalDateTime by lazy { currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGenerateRecipeBinding.inflate(inflater, container, false)
        val view = binding?.root

        val viewModelFactory = GenerateRecipeViewModelFactory(assistedFactory, WeakReference<IGenerateRecipeViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(GenerateRecipeViewModel::class.java)

        viewModel.init(datetime.hour)

        viewModel.getRecipe()

        return view
    }

    override fun updateState(state: GenerateRecipeState) {
        when (state) {
            is GenerateRecipeState.InitialState -> handleInitialState()
            is GenerateRecipeState.ErrorState -> handleErrorState(state)
            is GenerateRecipeState.LoadingState -> handleLoadingState()
            is GenerateRecipeState.ShowAlreadyBrewingState -> handleShowAlreadyBrewingState()
            is GenerateRecipeState.ShowRecipeState -> handleShowRecipeState(state)
            is GenerateRecipeState.RefreshRecipeState -> handleRefreshRecipeState(state)
            is GenerateRecipeState.StartTimerState -> handleStartTimerState(state)
            is GenerateRecipeState.HideResumeButtonState -> handleHideResumeButtonState()
            is GenerateRecipeState.ShowResumeButtonState -> handleShowResumeButtonState()
            is GenerateRecipeState.UpdateToolbarState -> handleUpdateToolbarState(state)
        }
    }

    override fun handleShowResumeButtonState() {
        ThreadUtil.runOnMain {
            if (fadeInAnimation.hasEnded().not()) {
                fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_out)
                binding?.resumeBrewButton?.startAnimation(fadeInAnimation)
            }
        }
    }

    override fun handleHideResumeButtonState() {
        binding?.resumeBrewButton?.fadeOut()
    }

    override fun handleInitialState() {
        fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.initiliaze_animation)
        adapterAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.adapter_anim)
        initListeners()
    }

    override fun handleLoadingState() {
        // Nothing yet
    }

    override fun handleErrorState(state: GenerateRecipeState.ErrorState) {
        // Nothing yet
    }

    override fun handleShowAlreadyBrewingState() {
        Notification.make(binding?.generateRecipeButton, R.string.alreadyBrewingDialog)
            .setAnchorView(R.id.resumeBrewButton)
            .show()
    }

    override fun handleShowRecipeState(state: GenerateRecipeState.ShowRecipeState) {
        binding?.recipeList?.adapter = GenerateRecipeListView(
            steps = state.steps,
            context = requireContext()
        )
    }

    override fun handleRefreshRecipeState(state: GenerateRecipeState.RefreshRecipeState) {
        binding?.recipeList?.startAnimation(adapterAnimation)
        MainScope().launch {
            delay(adapterAnimation.duration)
            binding?.recipeList?.adapter = GenerateRecipeListView(
                steps = state.steps,
                context = requireContext()
            )
        }
    }

    override fun handleStartTimerState(state: GenerateRecipeState.StartTimerState) {
        startActivity(TimerActivity.newIntent(requireContext(), state.recipe, state.flow))
    }

    override fun handleUpdateToolbarState(state: GenerateRecipeState.UpdateToolbarState) {
        binding?.toolbar?.title = resources.getString(state.title)
    }

    private fun initListeners() {
        binding?.apply {
            generateRecipeButton.setOnClickListener {
                viewModel.generateRecipe()
            }
            generateRecipeButton.setOnLongClickListener {
                viewModel.forceGenerateRecipe()
                true
            }
            startTimerButton.setOnClickListener {
                viewModel.startTimer(resume = false)
            }
            resumeBrewButton.setOnClickListener {
                viewModel.startTimer(resume = true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): GenerateRecipeFragment {
            val args = Bundle()
            val fragment = GenerateRecipeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
