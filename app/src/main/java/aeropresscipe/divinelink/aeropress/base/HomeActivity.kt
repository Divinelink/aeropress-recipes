package aeropresscipe.divinelink.aeropress.base

import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.snackbar.Notification
import aeropresscipe.divinelink.aeropress.databinding.ActivityHomeBinding
import androidx.core.content.ContextCompat
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment
import aeropresscipe.divinelink.aeropress.history.HistoryFragment
import aeropresscipe.divinelink.aeropress.history.HistoryState
import aeropresscipe.divinelink.aeropress.home.HomeState
import aeropresscipe.divinelink.aeropress.home.HomeStateHandler
import aeropresscipe.divinelink.aeropress.home.HomeViewModel
import aeropresscipe.divinelink.aeropress.home.HomeViewModelAssistedFactory
import aeropresscipe.divinelink.aeropress.home.HomeViewModelFactory
import aeropresscipe.divinelink.aeropress.home.IHomeViewModel
import aeropresscipe.divinelink.aeropress.timer.TimerActivity
import android.annotation.SuppressLint
import gr.divinelink.core.util.viewBinding.activity.viewBinding
import android.view.MenuItem
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import gr.divinelink.core.util.extensions.fadeOut
import gr.divinelink.core.util.utils.DimensionUnit
import gr.divinelink.core.util.utils.setNavigationBarColor
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity(),
    IHomeViewModel,
    HomeStateHandler,
    HistoryFragment.Callback {
    private val binding: ActivityHomeBinding by viewBinding()

    private val fragments: Array<out Fragment> get() = arrayOf(recipeFragment, favoritesFragment, historyFragment)
    private var selectedIndex = 0

    @Inject
    lateinit var assistedFactory: HomeViewModelAssistedFactory
    private lateinit var viewModel: HomeViewModel

    private lateinit var recipeFragment: GenerateRecipeFragment
    private lateinit var favoritesFragment: SavedRecipesFragment
    private lateinit var historyFragment: HistoryFragment

    @Px
    private val padding = DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_TIMER_VIEW).toInt()

    @Px
    private val recyclerViewPadding = DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_TIMER_VIEW_RECYCLER).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNavigationBarColor(ContextCompat.getColor(this, R.color.colorSurface2))
        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
//        binding.bottomNavigation.setOnItemReselectedListener(onItemReselectedListener)

        if (savedInstanceState == null) {
            recipeFragment = GenerateRecipeFragment.newInstance()
            favoritesFragment = SavedRecipesFragment.newInstance()
            historyFragment = HistoryFragment.newInstance()

            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, recipeFragment, RECIPE_TAG)
                .add(R.id.fragment, favoritesFragment, FAVORITES_TAG)
                .add(R.id.fragment, historyFragment, HISTORY_TAG)
                .commitNow()
        } else {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0)

            recipeFragment = supportFragmentManager.findFragmentByTag(RECIPE_TAG) as GenerateRecipeFragment
            favoritesFragment = supportFragmentManager.findFragmentByTag(FAVORITES_TAG) as SavedRecipesFragment
            historyFragment = supportFragmentManager.findFragmentByTag(HISTORY_TAG) as HistoryFragment
        }

        val selectedFragment = fragments[selectedIndex]

        selectFragment(selectedFragment)

        val viewModelFactory = HomeViewModelFactory(assistedFactory, WeakReference<IHomeViewModel>(this))
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        viewModel.init()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("Activity resume.")
        viewModel.resume()
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
            is HomeState.ShowResumeButtonState -> handleShowResumeButtonState()
            is HomeState.HideResumeButtonState -> handleHideResumeButtonState()
            is HomeState.StartTimerState -> handleStartTimerState(state)
        }
    }

    override fun handleInitialState() {
//        TODO("Not yet implemented")
    }

    override fun handleShowResumeButtonState() {
        binding.timerProgressView.visibility = View.VISIBLE
        recipeFragment.updateBottomPadding(padding)
        favoritesFragment.updateBottomPadding(recyclerViewPadding)
        historyFragment.updateBottomPadding(recyclerViewPadding)
    }

    override fun handleHideResumeButtonState() {
        binding.timerProgressView.fadeOut()
        recipeFragment.updateBottomPadding(DimensionUnit.DP.toPixels(0F).toInt())
        favoritesFragment.updateBottomPadding(DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_RECYCLER).toInt())
        historyFragment.updateBottomPadding(DimensionUnit.DP.toPixels(PAD_BOTTOM_OF_RECYCLER).toInt())
    }

    override fun handleStartTimerState(state: HomeState.StartTimerState) {
        launchActivity(
            intent = TimerActivity.newIntent(this, state.recipe, state.flow),
            onResult = {
                if (it.resultCode == RESULT_OK) {
                    handleHideResumeButtonState()
                } else {
                    handleShowResumeButtonState()
                }
            }
        )
    }

    override fun onSnackbarShow(state: HistoryState.ShowSnackBar) {
        val anchorView = if (binding.timerProgressView.visibility == View.GONE) binding.bottomNavigation else binding.timerProgressView
        Notification
            .make(binding.bottomNavigation, resources.getString(state.value.string, getString(state.value.favorites)))
            .setAnchorView(anchorView)
            .show()
    }
}

interface TimerViewCallback {
    fun updateBottomPadding(bottomPadding: Int)
}

