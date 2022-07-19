package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.FragmentHistoryBinding
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesAdapter
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.swipe.SwipeAction
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment(),
    HistoryStateHandler,
    IHistoryViewModel {
    private var binding: FragmentHistoryBinding? = null

    private var mFadeAnimation: Animation? = null

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

    @Inject
    lateinit var assistedFactory: HistoryViewModelAssistedFactory
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding?.root
        binding?.toolbar?.setNavigationOnClickListener { activity?.onBackPressed() }

        val viewModelFactory = HistoryViewModelFactory(assistedFactory, WeakReference<IHistoryViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        return view
    }

//    override fun showHistory(savedRecipes: List<History>) {
//            activity?.runOnUiThread(object : Runnable {
//                val historyRecipesRvAdapter = HistoryRecipesRvAdapter(savedRecipes, activity, historyRecipesRV)
//                override fun run() {
//                    mFadeAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites)
//                    historyRecipesRV!!.animation = mFadeAnimation
//                    historyRecipesRV!!.adapter = historyRecipesRvAdapter
//                    historyRecipesRvAdapter.setPresenter(presenter)
//                }
//            })
//    }
//
//    override fun showEmptyListMessage() {
//            activity?.runOnUiThread {
//                beginFading(historyRecipesRV, AnimationUtils.loadAnimation(activity, R.anim.fade_out_favourites), 8)
//                beginFading(mEmptyListLL, AnimationUtils.loadAnimation(activity, R.anim.fade_in_favourites), 0)
//                setIsHistoryEmptyBool(requireContext(), true)
//            }
//
//    }
//
//    fun beginFading(view: View?, animation: Animation?, visibility: Int) {
//        // 0 = View.VISIBLE, 4 = View.INVISIBLE, 8 = View.GONE
//        view?.startAnimation(animation)
//        view?.visibility = visibility
//    }
//
//    override fun passData(recipe: Recipe) {
//            activity?.runOnUiThread {
//                recipe.isNewRecipe = true
//                startActivity(newIntent(requireContext(), recipe, TimerFlow.START))
//            }
//        }

//
//    var toolbarMenuClickListener = Toolbar.OnMenuItemClickListener { item ->
// //        when (item.itemId) {
// ////            R.id.action_delete_history -> {
// ////                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
// ////                mHistoryIsEmpty = preferences.getBoolean("isHistoryEmpty", false)
// ////                TODO Make button unavailable when History is empty.
// ////                if (!mHistoryIsEmpty) showDeleteHistoryDialog()
// ////                true
// ////            }
// ////            else -> false
// //        }
//    }

//    fun showDeleteHistoryDialog() {
//        MaterialAlertDialogBuilder(context!!)
//            .setTitle(R.string.action_delete)
//            .setMessage(R.string.deleteHistoryMessage)
//            .setPositiveButton(R.string.delete) { dialogInterface: DialogInterface?, i: Int -> presenter!!.clearHistory(context) }
//            .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface?, i: Int -> }
//            .show()
//    }
//
//    override fun setIsHistoryEmptyBool(ctx: Context, bool: Boolean) {
//        val preferences = PreferenceManager.getDefaultSharedPreferences(ctx)
//        val editor = preferences.edit()
//        editor.putBoolean("isHistoryEmpty", bool)
//        editor.apply()
//    }
//
//    override fun setRecipeLiked(isLiked: Boolean, pos: Int) {
//            activity?.runOnUiThread { historyRecipesRV!!.adapter!!.notifyItemChanged(pos, isLiked) }
//    }

    override fun updateState(state: HistoryState) {
        when (state) {
            is HistoryState.ErrorState -> handleErrorState(state)
            is HistoryState.InitialState -> handleInitialState()
            is HistoryState.LoadingState -> handleLoadingState()
            is HistoryState.EmptyHistoryState -> handleEmptyHistoryState()
            is HistoryState.ShowHistoryState -> handleShowHistoryState(state)
            is HistoryState.StartNewBrewState -> handleStartNewBrewState(state)
            is HistoryState.RecipeLikedState -> handleRecipeLikedState(state)
        }
    }

    override fun handleInitialState() {
        binding?.historyRV?.layoutManager = LinearLayoutManager(activity)
        binding?.historyRV?.adapter = historyAdapter
    }

    override fun handleLoadingState() {
        // Intentionally Blank.
    }

    override fun handleErrorState(state: HistoryState.ErrorState) {
        // Intentionally Blank.
    }

    override fun handleShowHistoryState(state: HistoryState.ShowHistoryState) {
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
    }

    override fun handleStartNewBrewState(state: HistoryState.StartNewBrewState) {
        startActivity(TimerActivity.newIntent(requireContext(), state.recipe))
    }

    override fun handleRecipeLikedState(state: HistoryState.RecipeLikedState) {
        val items = historyAdapter.currentList.toMutableList()
        items[state.position] = state.item
        historyAdapter.submitList(items)
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
