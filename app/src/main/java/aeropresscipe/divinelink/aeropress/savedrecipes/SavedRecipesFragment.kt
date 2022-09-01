package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.HomeActivity.Companion.PAD_BOTTOM_OF_RECYCLER
import aeropresscipe.divinelink.aeropress.base.TimerViewCallback
import aeropresscipe.divinelink.aeropress.databinding.FragmentSavedRecipesBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.savedrecipes.adapter.EmptyType
import aeropresscipe.divinelink.aeropress.savedrecipes.adapter.FavoriteItem
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.padding
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.utils.DimensionUnit
import java.lang.ref.WeakReference
import javax.inject.Inject


@AndroidEntryPoint
class SavedRecipesFragment : Fragment(),
    SavedRecipesStateHandler,
    ISavedRecipesViewModel,
    TimerViewCallback {
    private var binding: FragmentSavedRecipesBinding? = null

    private lateinit var viewModel: SavedRecipesViewModel

    @Inject
    lateinit var assistedFactory: SavedTimerViewModelAssistedFactory

    private var mFadeAnimation: Animation? = null

    private val recipesAdapter = MappingAdapter()

    @Px
    private var recyclerViewPadding = DimensionUnit.PIXELS.toPixels(PAD_BOTTOM_OF_RECYCLER).toInt()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)
        val view = binding?.root

        val viewModelFactory = SavedRecipesViewModelFactory(assistedFactory, WeakReference<ISavedRecipesViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory)[SavedRecipesViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setNavigationOnClickListener { activity?.onBackPressed() }
        binding?.recyclerView?.padding(bottom = recyclerViewPadding)
        bindAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun updateState(state: SavedRecipesState) {
        when (state) {
            is SavedRecipesState.ErrorState -> handleErrorState()
            is SavedRecipesState.InitialState -> handleInitialState()
            is SavedRecipesState.LoadingState -> handleLoadingState()
            is SavedRecipesState.RecipesState -> handleRecipesState(state)
            is SavedRecipesState.EmptyRecipesState -> handleEmptyRecipesState()
            is SavedRecipesState.RecipeDeletedState -> handleRecipeDeletedState(state)
            is SavedRecipesState.StartNewBrewState -> handleStartNewBrew(state)
        }
    }

    override fun handleInitialState() {
        // Intentionally Blank.
    }

    override fun handleLoadingState() {
        // Intentionally Blank.
    }

    override fun handleErrorState() {
        // Intentionally Blank.
    }

    override fun handleRecipeDeletedState(state: SavedRecipesState.RecipeDeletedState) {
        recipesAdapter.submitList(state.recipes)
    }

    override fun handleStartNewBrew(state: SavedRecipesState.StartNewBrewState) {
        startActivity(TimerActivity.newIntent(requireContext(), state.recipe))
    }

    override fun handleEmptyRecipesState() {
        recipesAdapter.submitList(listOf(EmptyType.EmptyFavorites))
    }

    override fun handleRecipesState(state: SavedRecipesState.RecipesState) {
        mFadeAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites)
        binding?.recyclerView?.animation = mFadeAnimation
        recipesAdapter.submitList(state.recipes)
    }

    private fun showDeleteRecipeDialog(recipe: Recipe) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.deleteRecipeDialogTitle)
            .setMessage(R.string.deleteRecipeDialogMessage)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteRecipe(recipe)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun bindAdapter() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recipesAdapter
        }

        FavoriteItem.register(recipesAdapter,
            onActionClicked = { recipe: SavedRecipeDomain, swipeAction: SwipeAction ->
                when (swipeAction.actionId) {
                    R.id.delete -> showDeleteRecipeDialog(recipe.recipe)
                    R.id.brew -> viewModel.startBrew(recipe.recipe)
                }
            },
            actionBindHelper = ActionBindHelper()
        )
    }

    override fun updateBottomPadding(bottomPadding: Int) {
        recyclerViewPadding = bottomPadding
        binding?.recyclerView?.padding(bottom = bottomPadding)
    }


    companion object {
        @JvmStatic
        fun newInstance(): SavedRecipesFragment {
            val fragment = SavedRecipesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
