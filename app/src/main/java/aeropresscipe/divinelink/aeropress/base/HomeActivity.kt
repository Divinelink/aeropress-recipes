package aeropresscipe.divinelink.aeropress.base

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.snackbar.Notification
import aeropresscipe.divinelink.aeropress.databinding.ActivityHomeBinding
import aeropresscipe.divinelink.aeropress.recipe.RecipeFragment
import aeropresscipe.divinelink.aeropress.recipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.HistoryFragment
import aeropresscipe.divinelink.aeropress.history.HistoryState
import aeropresscipe.divinelink.aeropress.home.HomeState
import aeropresscipe.divinelink.aeropress.home.HomeStateHandler
import aeropresscipe.divinelink.aeropress.home.HomeViewModel
import aeropresscipe.divinelink.aeropress.home.HomeViewModelAssistedFactory
import aeropresscipe.divinelink.aeropress.home.HomeViewModelFactory
import aeropresscipe.divinelink.aeropress.home.IHomeViewModel
import aeropresscipe.divinelink.aeropress.favorites.FavoritesFragment
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import aeropresscipe.divinelink.aeropress.timer.TimerFlow
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
import gr.divinelink.core.util.extensions.addBackPressCallback
import gr.divinelink.core.util.utils.DimensionUnit
import gr.divinelink.core.util.utils.setNavigationBarColor
import gr.divinelink.core.util.viewBinding.activity.viewBinding
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(),
    IHomeViewModel,
    HomeStateHandler,
    HistoryFragment.Callback {
    private val binding: ActivityHomeBinding by viewBinding()

    private val fragments: Array<out Fragment> get() = arrayOf(recipeFragment, favoritesFragment, historyFragment)
    private var selectedIndex = 0

    private lateinit var recipeFragment: RecipeFragment
    private lateinit var favoritesFragment: FavoritesFragment
    private lateinit var historyFragment: HistoryFragment

    @Inject
    lateinit var assistedFactory: HomeViewModelAssistedFactory
    private lateinit var viewModel: HomeViewModel

//    private val dynamicTheme: DynamicTheme = DynamicNoActionBarTheme()

    @Px
    private val padding = DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_TIMER_VIEW).toInt()

    @Px
    private val recyclerViewPadding = DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_TIMER_VIEW_RECYCLER).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        dynamicTheme.onCreate(this)
        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorSurface2))
        onBackPressedDispatcher.addBackPressCallback(this) { onBackPressed() }
        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
//        binding.bottomNavigation.setOnItemReselectedListener(onItemReselectedListener)

        if (savedInstanceState == null) {
            recipeFragment = RecipeFragment.newInstance()
            favoritesFragment = FavoritesFragment.newInstance()
            historyFragment = HistoryFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, recipeFragment, RECIPE_TAG)
                .add(R.id.fragment, favoritesFragment, FAVORITES_TAG)
                .add(R.id.fragment, historyFragment, HISTORY_TAG)
                .commitNow()
        } else {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0)

            recipeFragment = supportFragmentManager.findFragmentByTag(RECIPE_TAG) as RecipeFragment
            favoritesFragment = supportFragmentManager.findFragmentByTag(FAVORITES_TAG) as FavoritesFragment
            historyFragment = supportFragmentManager.findFragmentByTag(HISTORY_TAG) as HistoryFragment
        }

        val selectedFragment = fragments[selectedIndex]

        selectFragment(selectedFragment)

        val viewModelFactory = HomeViewModelFactory(assistedFactory, WeakReference<IHomeViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        viewModel.delegate = WeakReference(this)

        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
//        dynamicTheme.onResume(this)
        Timber.d("Activity resume.")
        viewModel.resume()
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
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()
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

    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId == R.id.recipe) {
            finish()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.recipe
        }
    }

    override fun onUpdateRecipe(recipe: Recipe, flow: TimerFlow, update: Boolean) {
        Timber.d("Recipe updated. Set refresh to true.")
        recipeFragment.refresh = true
        viewModel.startTimer(recipe, flow, update)
    }
}

interface TimerViewCallback {
    fun updateBottomPadding(bottomPadding: Int)
}
