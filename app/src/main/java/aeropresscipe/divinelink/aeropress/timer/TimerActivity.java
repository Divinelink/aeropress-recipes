package aeropresscipe.divinelink.aeropress.timer;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


public class TimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Toolbar mToolBar = findViewById(R.id.toolbar);
        mToolBar.setNavigationOnClickListener(v1 -> onBackPressed());

        Recipe dice = (Recipe) getIntent().getSerializableExtra("timer");

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.timerRoot, TimerFragment.newInstance(dice))
                .commit();
    }


}