package aeropresscipe.divinelink.aeropress.base;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.history.HistoryFragment;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment;
import dagger.hilt.android.AndroidEntryPoint;
import gr.divinelink.core.util.utils.WindowUtil;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
        mBottomNavigationView.setOnItemReselectedListener(onItemReselectedListener);

        WindowUtil.INSTANCE.setNavigationBarColor(this, ContextCompat.getColor(this, R.color.colorSurface2));

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment, GenerateRecipeFragment.newInstance())
                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // When leaving from Timer Activity, set bottomNavigation to be the recipe button
        // and restart the fragment, so we can see the resume button flashing.
        mBottomNavigationView.setSelectedItemId(R.id.recipe);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .replace(R.id.fragment, GenerateRecipeFragment.newInstance())
                .commit();
    }


    public void addGenerateRecipeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, GenerateRecipeFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void addSavedRecipesFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, SavedRecipesFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void addHistoryFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, HistoryFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    NavigationBarView.OnItemSelectedListener onItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.recipe:
                        addGenerateRecipeFragment();
                        return true;
                    case R.id.favorites:
                        addSavedRecipesFragment();
                        return true;
                    case R.id.history:
                        addHistoryFragment();
                        return true;
                }
                return false;
            };

    NavigationBarView.OnItemReselectedListener onItemReselectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.recipe:
                // do nothing
            case R.id.favorites:
                // do nothing
            case R.id.history:
                // do nothing
        }
    };


    @Override
    public void onBackPressed() {
        if (mBottomNavigationView.getSelectedItemId() == R.id.recipe) {
            super.onBackPressed();
            finish();
        } else {
            mBottomNavigationView.setSelectedItemId(R.id.recipe);
        }
    }
}
