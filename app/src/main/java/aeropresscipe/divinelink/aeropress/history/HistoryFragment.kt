package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentHistoryBinding
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesAdapter
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.swipe.SwipeAction
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment(),
    HistoryStateHandler,
    IHistoryViewModel {
    private var binding: FragmentHistoryBinding? = null

    private var clearMenuItem: MenuItem? = null

    private var mFadeAnimation: Animation? = null

    @Inject
    lateinit var assistedFactory: HistoryViewModelAssistedFactory
    private lateinit var viewModel: HistoryViewModel

    private val historyAdapter by lazy {
        SavedRecipesAdapter(
            requireContext(),
            onActionClicked = { recipe: Any, swipeAction: SwipeAction ->
                recipe as History
                when (swipeAction.actionId) {
                    R.id.brew -> viewModel.startBrew(recipe.recipe)
                }
            },
            onLike = { recipe: Any, position: Int ->
                recipe as History
                viewModel.likeRecipe(recipe, position)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding?.root
        binding?.toolbar?.setNavigationOnClickListener { activity?.onBackPressed() }

        val viewModelFactory = HistoryViewModelFactory(assistedFactory, WeakReference<IHistoryViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        return view
    }

    override fun updateState(state: HistoryState) {
        when (state) {
            is HistoryState.ErrorState -> handleErrorState(state)
            is HistoryState.InitialState -> handleInitialState()
            is HistoryState.LoadingState -> handleLoadingState()
            is HistoryState.EmptyHistoryState -> handleEmptyHistoryState()
            is HistoryState.ShowHistoryState -> handleShowHistoryState(state)
            is HistoryState.StartNewBrewState -> handleStartNewBrewState(state)
            is HistoryState.RecipeLikedState -> handleRecipeLikedState(state)
            is HistoryState.ClearHistoryPopUpState -> handleClearHistoryPopUpState()
        }
    }

    override fun handleInitialState() {
        binding?.historyRV?.layoutManager = LinearLayoutManager(activity)
        binding?.historyRV?.adapter = historyAdapter

        binding?.toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_clear -> {
                    viewModel.clearHistory(false)
                    true
                }
                else -> false
            }
        }
    }

    override fun handleLoadingState() {
        // Intentionally Blank.
    }

    override fun handleErrorState(state: HistoryState.ErrorState) {
        // Intentionally Blank.
    }

    override fun handleShowHistoryState(state: HistoryState.ShowHistoryState) {
        if (clearMenuItem?.isEnabled == false) {
            clearMenuItem?.isEnabled = true
        }
        mFadeAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites)
        binding?.historyRV?.animation = mFadeAnimation
        historyAdapter.submitList(state.list)
    }

    override fun handleEmptyHistoryState() {
        beginFading(
            binding?.historyRV,
            AnimationUtils.loadAnimation(activity, R.anim.fade_out_favourites),
            View.GONE
        )
        beginFading(
            binding?.emptyListLayout,
            AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites),
            View.VISIBLE
        )
        clearMenuItem?.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history, menu)
        clearMenuItem = menu.findItem(R.id.menu_clear)
//        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun handleStartNewBrewState(state: HistoryState.StartNewBrewState) {
        startActivity(TimerActivity.newIntent(requireContext(), state.recipe))
    }

    override fun handleRecipeLikedState(state: HistoryState.RecipeLikedState) {
        val items = historyAdapter.currentList.toMutableList()
        items[state.position] = state.item
        historyAdapter.submitList(items)
    }

    override fun handleClearHistoryPopUpState() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.action_delete)
            .setMessage(R.string.deleteHistoryMessage)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.clearHistory(true)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                // Intentionally Blank.
            }
            .show()
    }

    private fun beginFading(view: View?, animation: Animation, visibility: Int) {
        view?.startAnimation(animation)
        view?.visibility = visibility
    }

    companion object {
        @JvmStatic
        fun newInstance(): HistoryFragment {
            val fragment = HistoryFragment()
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
