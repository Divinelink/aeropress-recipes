package aeropresscipe.divinelink.aeropress.timer;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class TimerActivity extends AppCompatActivity {

    private static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    public static Intent newIntent(@NonNull Context context,
                                   @NonNull Recipe recipe) {
        Intent intent = new Intent(context, TimerActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Toolbar mToolBar = findViewById(R.id.toolbar);
        mToolBar.setNavigationOnClickListener(v1 -> onBackPressed());

        Recipe recipe = (Recipe) getIntent().getSerializableExtra(EXTRA_RECIPE);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.timerRoot, TimerFragment.newInstance(recipe))
                .commit();
    }


}