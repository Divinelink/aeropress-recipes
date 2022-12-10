package aeropresscipe.divinelink.aeropress.favorites

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.base.HomeActivity.Companion.PAD_BOTTOM_OF_RECYCLER
import aeropresscipe.divinelink.aeropress.base.TimerViewCallback
import aeropresscipe.divinelink.aeropress.databinding.FragmentSavedRecipesBinding
import aeropresscipe.divinelink.aeropress.favorites.adapter.EmptyType
import aeropresscipe.divinelink.aeropress.favorites.adapter.RecipesAdapter
import aeropresscipe.divinelink.aeropress.favorites.ui.FavoritesViewState
import aeropresscipe.divinelink.aeropress.history.HistoryFragment
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.padding
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.utils.DimensionUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment :
    Fragment(),
    TimerViewCallback {
    private var binding: FragmentSavedRecipesBinding? = null

    private lateinit var callback: HistoryFragment.Callback
    private val viewModel: FavoritesViewModel by viewModels()

    private val recipesAdapter = RecipesAdapter(
        onActionClicked = { recipe: Favorites, swipeAction: SwipeAction ->
            when (swipeAction.actionId) {
                R.id.delete -> showDeleteRecipeDialog(recipe.recipe)
                R.id.brew -> viewModel.startBrewClicked(recipe.recipe)
            }
        },
        actionBindHelper = ActionBindHelper()
    )

    @Px
    private var recyclerViewPadding = DimensionUnit.PIXELS.toPixels(PAD_BOTTOM_OF_RECYCLER).toInt()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setNavigationOnClickListener { callback.onBackPressed() }
        binding?.recyclerView?.padding(bottom = recyclerViewPadding)
        bindAdapter()

        collectLatestLifecycleFlow(
            viewModel.viewState
        ) { state ->
            if (state.brewRecipe != null) {
                callback.onUpdateRecipe(state.brewRecipe, TimerFlow.START, true)
            }

            if (state.emptyRecipes == true) {
                recipesAdapter.submitList(listOf(EmptyType.EmptyFavorites))
            }

            if (state.recipes != null) {
                handleRecipesState(state)
            }

            /*
            if (state.isLoading) {
                // to do add loading state
            }
             */
        }
    }

    private fun handleRecipesState(state: FavoritesViewState) {
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
    }

    override fun updateBottomPadding(bottomPadding: Int) {
        recyclerViewPadding = bottomPadding
        binding?.recyclerView?.padding(bottom = bottomPadding)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FavoritesFragment {
            val fragment = FavoritesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as HistoryFragment.Callback
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

fun <T> Fragment.collectLatestLifecycleFlow(
    flow: Flow<T>,
    collect: suspend (T) -> Unit,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}
