package aeropresscipe.divinelink.aeropress.generaterecipe

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.Notification
import aeropresscipe.divinelink.aeropress.databinding.FragmentGenerateRecipeBinding
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.fadeOut
import gr.divinelink.core.util.utils.ThreadUtil
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    //FIXME Recycler view Adapter needs fix

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGenerateRecipeBinding.inflate(inflater, container, false)
        val view = binding?.root
        binding?.recipeRv?.layoutManager = LinearLayoutManager(activity)

        val viewModelFactory = GenerateRecipeViewModelFactory(assistedFactory, WeakReference<IGenerateRecipeViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(GenerateRecipeViewModel::class.java)

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
        ThreadUtil.runOnMain {
            binding?.resumeBrewButton?.fadeOut()
        }
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
        binding?.recipeRv?.adapter = GenerateRecipeRvAdapter(state.recipe, requireContext())
    }

    override fun handleRefreshRecipeState(state: GenerateRecipeState.RefreshRecipeState) {
        binding?.recipeRv?.startAnimation(adapterAnimation)
        MainScope().launch {
            delay(adapterAnimation.duration)
            binding?.recipeRv?.adapter = GenerateRecipeRvAdapter(state.recipe, requireContext())
        }
    }

    override fun handleStartTimerState(state: GenerateRecipeState.StartTimerState) {
        startActivity(TimerActivity.newIntent(requireContext(), state.recipe))
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