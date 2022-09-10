package aeropresscipe.divinelink.aeropress.base

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.snackbar.Notification
import aeropresscipe.divinelink.aeropress.databinding.ActivityHomeBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.HistoryFragment
import aeropresscipe.divinelink.aeropress.history.HistoryState
import aeropresscipe.divinelink.aeropress.home.HomeState
import aeropresscipe.divinelink.aeropress.home.HomeStateHandler
import aeropresscipe.divinelink.aeropress.home.HomeViewModel
import aeropresscipe.divinelink.aeropress.home.HomeViewModelAssistedFactory
import aeropresscipe.divinelink.aeropress.home.HomeViewModelFactory
import aeropresscipe.divinelink.aeropress.home.IHomeViewModel
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
import aeropresscipe.divinelink.aeropress.util.DynamicNoActionBarTheme
import aeropresscipe.divinelink.aeropress.util.DynamicTheme
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.utils.DimensionUnit
import gr.divinelink.core.util.utils.setNavigationBarColor
import gr.divinelink.core.util.viewBinding.activity.viewBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(),
    IHomeViewModel,
    HomeStateHandler,
    HistoryFragment.Callback {
    private val binding: ActivityHomeBinding by viewBinding()

    private var recipeFragment = GenerateRecipeFragment.newInstance()
    private var favoritesFragment = SavedRecipesFragment.newInstance()
    private var historyFragment = HistoryFragment.newInstance()

    private val fragments: Array<out Fragment> get() = arrayOf(recipeFragment, favoritesFragment, historyFragment)
    private var selectedIndex = 0

    @Inject
    lateinit var assistedFactory: HomeViewModelAssistedFactory
    private lateinit var viewModel: HomeViewModel

    private val dynamicTheme: DynamicTheme = DynamicNoActionBarTheme() // app is shown correctly when saveInstanceState = null

    @Px
    private val padding = DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_TIMER_VIEW).toInt()

    @Px
    private val recyclerViewPadding = DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_TIMER_VIEW_RECYCLER).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        dynamicTheme.onCreate(this)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorSurface2))
        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
//        binding.bottomNavigation.setOnItemReselectedListener(onItemReselectedListener)

//        recipeFragment = GenerateRecipeFragment.newInstance()
//        favoritesFragment = SavedRecipesFragment.newInstance()
//        historyFragment = HistoryFragment.newInstance()
        if (savedInstanceState == null) {
            // nothing :/
        } else {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0)
        }
//        recipeFragment = GenerateRecipeFragment.newInstance()
//        favoritesFragment = SavedRecipesFragment.newInstance()
//        historyFragment = HistoryFragment.newInstance()

        val selectedFragment = fragments[selectedIndex]

        selectFragment(selectedFragment)

        val viewModelFactory = HomeViewModelFactory(assistedFactory, this)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
//        dynamicTheme.onResume(this)
        Timber.d("Activity resume.")
//        viewModel.resume()
//       When leaving from Timer Activity, set bottomNavigation to be the recipe button
//       and restart the fragment, so we can see the resume button flashing.
//        binding.bottomNavigation.selectedItemId = R.id.recipe
//        selectFragment(recipeFragment)
    }

    private var onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.recipe -> {
                selectFragment(recipeFragment)
                return@OnItemSelectedListener true
            }
            R.id.favorites -> {
                selectFragment(favoritesFragment)
                return@OnItemSelectedListener true
            }
            R.id.history -> {
                selectFragment(historyFragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }
    /* todo add callback that scrolls to top
    var onItemReselectedListener = OnItemReselectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.recipe, R.id.favorites, R.id.history -> {
                // Intentionally Blank.
            }
        }
    }
     */

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putInt(SELECTED_INDEX, selectedIndex)
    }

    @SuppressLint("DetachAndAttachSameFragment")
    private fun selectFragment(selectedFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, selectedFragment, selectedFragment.tag)
            commit()
        }
    }

    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId == R.id.recipe) {
            super.onBackPressed()
            finish()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.recipe
        }
    }

    companion object {
        private const val RECIPE_TAG = "RECIPE"
        private const val FAVORITES_TAG = "FAVORITES"
        private const val HISTORY_TAG = "HISTORY"
        private const val SELECTED_INDEX = "SELECTED_INDEX"

        @Dimension(unit = Dimension.DP)
        const val PAD_BOTTOM_OF_RECYCLER = 16F

        @Dimension(unit = Dimension.DP)
        private const val PAD_BOTTOM_OF_TIMER_VIEW = 60F

        @Dimension(unit = Dimension.DP)
        private const val PAD_BOTTOM_OF_TIMER_VIEW_RECYCLER = 68F
    }

    override fun updateState(state: HomeState) {
        when (state) {
            is HomeState.InitialState -> handleInitialState()
            is HomeState.ShowResumeButtonState -> handleShowResumeButtonState(state)
            is HomeState.HideResumeButtonState -> handleHideResumeButtonState()
            is HomeState.StartTimerState -> handleStartTimerState(state)
        }
    }

    override fun handleInitialState() {
        binding.timerProgressView.setOnClickListener { viewModel.resumeTimer() }
    }

    override fun handleShowResumeButtonState(state: HomeState.ShowResumeButtonState) {
        binding.timerProgressView.apply {
            doOnAttach {
                setTimerView(
                    dice = state.dice,
                    onViewAttached = {
                        recipeFragment.updateBottomPadding(padding)
                        favoritesFragment.updateBottomPadding(recyclerViewPadding)
                        historyFragment.updateBottomPadding(recyclerViewPadding)
                    }
                )
            }
        }
    }

    override fun handleHideResumeButtonState() {
        binding.timerProgressView.dispose()
        recipeFragment.updateBottomPadding(DimensionUnit.DP.toPixels(0F).toInt())
        favoritesFragment.updateBottomPadding(DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_RECYCLER).toInt())
        historyFragment.updateBottomPadding(DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_RECYCLER).toInt())
    }

    override fun handleStartTimerState(state: HomeState.StartTimerState) {
        startActivity(TimerActivity.newIntent(this, state.recipe, state.flow))
    }

    override fun onSnackbarShow(state: HistoryState.ShowSnackBar) {
        val anchorView = if (binding.timerProgressView.visibility == View.GONE) binding.bottomNavigation else binding.timerProgressView
        Notification
            .make(binding.bottomNavigation, resources.getString(state.value.string, getString(state.value.favorites)))
            .setAnchorView(anchorView)
            .show()
    }

    override fun onUpdateRecipe(recipe: Recipe, flow: TimerFlow, update: Boolean) {
        Timber.d("Recipe updated. Set refresh to true.")
        recipeFragment.refresh = true
        viewModel.startTimer(recipe, flow, update)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Suppress("UnusedPrivateMember")
    fun onTimerFinished(event: TimerExitEvent) {
        viewModel.resume()
    }
}

interface TimerViewCallback {
    fun updateBottomPadding(bottomPadding: Int)
}

class TimerExitEvent
