package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.timer.TimerActivity.Companion.newIntent
import aeropresscipe.divinelink.aeropress.customviews.Notification.Companion.make
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentGenerateRecipeBinding
import android.os.Looper
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import gr.divinelink.core.util.utils.ThreadUtil

class GenerateRecipeFragment : Fragment(), GenerateRecipeView {
    private var binding: FragmentGenerateRecipeBinding? = null

    private lateinit var fadeInAnimation: Animation
    private lateinit var adapterAnimation: Animation

    private var presenter: GenerateRecipePresenter? = null
    private var recipe: Recipe? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGenerateRecipeBinding.inflate(inflater, container, false)
        val view = binding?.root
        binding?.recipeRv?.layoutManager = LinearLayoutManager(activity)
        presenter = GenerateRecipePresenterImpl(this)
        presenter?.getRecipe(context)
        fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.initiliaze_animation)
        adapterAnimation = AnimationUtils.loadAnimation(activity, R.anim.adapter_anim)
        initListeners()

        return view
    }

    private fun initListeners() {

        binding?.generateRecipeButton?.setOnClickListener {
            presenter?.getNewRecipe(context, false)
        }
        binding?.generateRecipeButton?.setOnLongClickListener {
            presenter?.getNewRecipe(context, true)
            true
        }
        binding?.startTimerButton?.setOnClickListener {
            recipe?.isNewRecipe = true
            startActivity(newIntent(requireContext(), recipe))
        }
        binding?.resumeBrewButton?.setOnClickListener {
            recipe?.isNewRecipe = false
            startActivity(newIntent(requireContext(), recipe))
        }
    }

    override fun showRecipe(randomRecipe: Recipe) {
        ThreadUtil.runOnMain {
            val recipeRvAdapter = GenerateRecipeRvAdapter(randomRecipe, activity)
            binding?.recipeRv?.adapter = recipeRvAdapter
            if (fadeInAnimation.hasEnded().not()) {
                fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_out)
                binding?.resumeBrewButton?.startAnimation(fadeInAnimation)
            }
        }
    }

    override fun passData(recipe: Recipe) {
        this.recipe = recipe
    }

    override fun showIsAlreadyBrewingDialog() {
        make(binding?.generateRecipeButton, R.string.alreadyBrewingDialog).setAnchorView(R.id.resumeBrewButton).show()
    }

    override fun showRecipeRemoveResume(randomRecipe: Recipe) {
        ThreadUtil.runOnMain {
            val recipeRvAdapter = GenerateRecipeRvAdapter(randomRecipe, activity)
            binding?.recipeRv?.startAnimation(adapterAnimation)
            // We need this so the adapter changes during the animation phase, and not before it.
            val adapterHandler = Handler(Looper.getMainLooper())
            val adapterRunnable = Runnable { binding?.recipeRv?.adapter = recipeRvAdapter }
            adapterHandler.postDelayed(adapterRunnable, adapterAnimation.duration)
            if (fadeInAnimation.hasEnded().not()) {
                fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
                binding?.resumeBrewButton?.startAnimation(fadeInAnimation)
            }
        }
    }

    override fun showRecipeAppStarts(randomRecipe: Recipe) {
        activity?.runOnUiThread(object : Runnable {
            val recipeRvAdapter = GenerateRecipeRvAdapter(randomRecipe, activity)
            override fun run() {
                binding?.recipeRv?.adapter = recipeRvAdapter
                binding?.resumeBrewButton?.startAnimation(fadeInAnimation)
            }
        })
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