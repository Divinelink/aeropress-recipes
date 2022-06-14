package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.base.HomeView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentSavedRecipesBinding
import gr.divinelink.core.util.swipe.SwipeAction
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI
import aeropresscipe.divinelink.aeropress.savedrecipes.util.SavedRecipesViewModelFactory
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.ref.WeakReference


class SavedRecipesFragment : Fragment(),
    SavedRecipesView,
    SavedRecipesStateHandler,
    ISavedRecipesViewModel {
    private var binding: FragmentSavedRecipesBinding? = null

    private lateinit var viewModel: SavedRecipesViewModel
    private lateinit var viewModelFactory: SavedRecipesViewModelFactory

    private var presenter: SavedRecipesPresenter? = null
    private var homeView: HomeView? = null
    private var mFadeAnimation: Animation? = null

    private val recipesAdapter by lazy {
        SavedRecipesAdapter(
            requireContext()
        ) { recipe: SavedRecipeDomain?, swipeAction: SwipeAction ->
            when (swipeAction.actionId) {
                R.id.delete -> showDeleteRecipeDialog(recipe)
                R.id.brew -> presenter?.startBrew(requireContext(), recipe)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)
        homeView = arguments?.getSerializable("home_view") as HomeView?
        val view = binding?.root

        viewModelFactory = SavedRecipesViewModelFactory(
            app = requireActivity().application,
            delegate = WeakReference<ISavedRecipesViewModel>(this),
            repository = SavedRecipesRepository(),
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(SavedRecipesViewModel::class.java)

        val layoutManager = LinearLayoutManager(activity)
        binding?.savedRecipesRV?.layoutManager = layoutManager
        presenter = SavedRecipesPresenterImpl(this)
        viewModel.getSavedRecipes()

        return view

    }

    override fun updateState(state: SavedRecipesState) {
        Log.d("State is: $state", "$state")
        when (state) {
            is SavedRecipesState.ErrorState -> handleErrorState()
            is SavedRecipesState.InitialState -> handleInitialState()
            is SavedRecipesState.LoadingState -> handleLoadingState()
            is SavedRecipesState.RecipesState -> handleRecipesState(state)
            is SavedRecipesState.EmptyRecipesState -> handleEmptyRecipesState()
        }
    }

    override fun handleInitialState() {
//        TODO("Not yet implemented")
    }

    override fun handleLoadingState() {
//        TODO("Not yet implemented")
    }

    override fun handleErrorState() {
//        TODO("Not yet implemented")
    }

    override fun handleEmptyRecipesState() {
        beginFading(
            binding?.savedRecipesRV,
            AnimationUtils.loadAnimation(activity, R.anim.fade_out_favourites),
            View.GONE
        )
        beginFading(
            binding?.emptyListLayout,
            AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites),
            View.VISIBLE
        )
    }

    override fun handleRecipesState(state: SavedRecipesState.RecipesState) {
        mFadeAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites)
        binding?.savedRecipesRV?.animation = mFadeAnimation
        binding?.savedRecipesRV?.adapter = recipesAdapter
        recipesAdapter.submitList(state.recipes)
    }

    override fun showSavedRecipes(savedRecipes: List<SavedRecipeDomain>) {
        activity?.runOnUiThread {
            mFadeAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites)
            binding?.savedRecipesRV?.animation = mFadeAnimation
            binding?.savedRecipesRV?.adapter = recipesAdapter
            recipesAdapter.submitList(savedRecipes)
        }
    }

    override fun showSavedRecipesAfterDeletion(
        savedRecipes: List<SavedRecipeDomain>,
    ) {
        activity?.runOnUiThread {
            recipesAdapter.submitList(savedRecipes)
        }
    }

    override fun passData(bloomTime: Int, brewTime: Int, bloomWater: Int, brewWater: Int) {
        activity?.runOnUiThread {
            val diceUI = DiceUI(bloomTime, brewTime, bloomWater, brewWater)
            diceUI.isNewRecipe = true
            homeView?.startTimerActivity(diceUI)
        }
    }


    override fun showEmptyListMessage() {
        activity?.runOnUiThread {
            beginFading(
                binding?.savedRecipesRV,
                AnimationUtils.loadAnimation(activity, R.anim.fade_out_favourites),
                View.GONE
            )
            beginFading(
                binding?.emptyListLayout,
                AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites),
                View.VISIBLE
            )
        }
    }

    private fun beginFading(view: View?, animation: Animation, visibility: Int) {
        view?.startAnimation(animation)
        view?.visibility = visibility
    }


    private fun showDeleteRecipeDialog(recipe: SavedRecipeDomain?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.deleteRecipeDialogTitle)
            .setMessage(R.string.deleteRecipeDialogMessage)
            .setPositiveButton(R.string.delete) { _, _ ->
                presenter?.deleteRecipe(requireContext(), recipe)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(homeView: HomeView?): SavedRecipesFragment {
            val fragment = SavedRecipesFragment()
            val args = Bundle()
            args.putSerializable("home_view", homeView)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}