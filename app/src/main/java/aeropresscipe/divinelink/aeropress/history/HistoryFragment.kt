package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentHistoryBinding
import aeropresscipe.divinelink.aeropress.savedrecipes.adapter.RecipesAdapter
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.setDisabled
import gr.divinelink.core.util.extensions.setEnabled
import gr.divinelink.core.util.swipe.SwipeAction
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment(),
    HistoryStateHandler,
    IHistoryViewModel {
    private var binding: FragmentHistoryBinding? = null

    private var clearMenuItem: MenuItem? = null

    @Inject
    lateinit var assistedFactory: HistoryViewModelAssistedFactory
    private lateinit var viewModel: HistoryViewModel

    private val historyAdapter by lazy {
        RecipesAdapter(
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
        clearMenuItem = binding?.toolbar?.menu?.findItem(R.menu.history)
        val viewModelFactory = HistoryViewModelFactory(assistedFactory, WeakReference<IHistoryViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        binding?.toolbar?.inflateMenu(R.menu.history)

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
        historyAdapter.submitList(state.list)
        updateToolbar(true)
    }

    override fun handleEmptyHistoryState() {
        historyAdapter.submitList(listOf(RecipesAdapter.EmptyHistory))
        updateToolbar(false)
    }

    private fun updateToolbar(enable: Boolean) {
        clearMenuItem = binding?.toolbar?.menu?.findItem(R.id.menu_clear)
        when (enable) {
            true -> clearMenuItem?.setEnabled()
            false -> clearMenuItem?.setDisabled()
        }
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
