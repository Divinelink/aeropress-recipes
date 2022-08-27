package aeropresscipe.divinelink.aeropress.base

import gr.divinelink.core.util.utils.WindowUtil.setNavigationBarColor
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
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationBarView.OnItemReselectedListener

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private val binding: ActivityHomeBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorSurface2))

        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
        binding.bottomNavigation.setOnItemReselectedListener(onItemReselectedListener)

        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment, GenerateRecipeFragment.newInstance())
            .commit()

    }

    override fun onResume() {
        super.onResume()
//         When leaving from Timer Activity, set bottomNavigation to be the recipe button
//         and restart the fragment, so we can see the resume button flashing.
        binding.bottomNavigation.selectedItemId = R.id.recipe
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_NONE)
            .replace(R.id.fragment, GenerateRecipeFragment.newInstance())
            .commit()
    }

    private fun addGenerateRecipeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, GenerateRecipeFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun addSavedRecipesFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, SavedRecipesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun addHistoryFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment, HistoryFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private var onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.recipe -> {
                addGenerateRecipeFragment()
                return@OnItemSelectedListener true
            }
            R.id.favorites -> {
                addSavedRecipesFragment()
                return@OnItemSelectedListener true
            }
            R.id.history -> {
                addHistoryFragment()
                return@OnItemSelectedListener true
            }
        }
        false
    }
    var onItemReselectedListener = OnItemReselectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.recipe, R.id.favorites, R.id.history -> {
                // Intentionally Blank.
            }
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
}