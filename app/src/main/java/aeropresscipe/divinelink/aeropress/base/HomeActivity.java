package aeropresscipe.divinelink.aeropress.base;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment;
import aeropresscipe.divinelink.aeropress.timer.TimerFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment;
import androidx.fragment.app.FragmentTransaction;


public class HomeActivity extends AppCompatActivity implements HomeView, Parcelable {
    //FIXME make sure Parcelable is absolutely necessary, otherwise remove it and try to fix the bug it creates where app crashes when closing the app.
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //TODO make it also change the bottomNav menu options when changing fragment from backbuttons.
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemReSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.homeRoot, GenerateRecipeFragment.newInstance(this))
                .commit();


    }


    @Override
    public void addGenerateRecipeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.homeRoot, GenerateRecipeFragment.newInstance(this))
                .commit();
    }

    @Override
    public void addTimerFragment(DiceUI diceUI) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.homeRoot, TimerFragment.newInstance(diceUI))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addTimerFragmentFromResume(DiceUI diceUI) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.homeRoot, TimerFragment.newInstance(diceUI))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addSavedRecipesFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.homeRoot, SavedRecipesFragment.newInstance(this))
                .addToBackStack(null)
                .commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.recipe:
                            addGenerateRecipeFragment();
                            return true;
                        case R.id.favourites:
                            addSavedRecipesFragment();
                            return true;
                        case R.id.history:
//                            openFragment(NotificationFragment.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

    BottomNavigationView.OnNavigationItemReselectedListener navigationItemReSelectedListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.recipe:
//                        addGenerateRecipeFragment();
                        case R.id.favourites:
                            //TODO make Favourites adapter reload and start from the top!
//                        addSavedRecipesFragment();
                        case R.id.history:
//                        openFragment(NotificationFragment.newInstance("", ""));
                    }
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
