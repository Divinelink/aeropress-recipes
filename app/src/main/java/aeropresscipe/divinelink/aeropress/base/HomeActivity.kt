package aeropresscipe.divinelink.aeropress.base

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ActivityHomeBinding
import androidx.core.content.ContextCompat
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment
import aeropresscipe.divinelink.aeropress.history.HistoryFragment
import gr.divinelink.core.util.viewBinding.activity.viewBinding
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import gr.divinelink.core.util.utils.setNavigationBarColor

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by viewBinding()

    private val fragments: Array<out Fragment> get() = arrayOf(recipeFragment, favoritesFragment, historyFragment)
    private var selectedIndex = 0

    private lateinit var recipeFragment: GenerateRecipeFragment
    private lateinit var favoritesFragment: SavedRecipesFragment
    private lateinit var historyFragment: HistoryFragment

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
    }

    override fun onResume() {
        super.onResume()
//       When leaving from Timer Activity, set bottomNavigation to be the recipe button
//       and restart the fragment, so we can see the resume button flashing.
        binding.bottomNavigation.selectedItemId = R.id.recipe
        selectFragment(recipeFragment)
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
    }
}
