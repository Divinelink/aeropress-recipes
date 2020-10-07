package aeropresscipe.divinelink.aeropress.base;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import aeropresscipe.divinelink.aeropress.timer.TimerFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment;

public class HomeActivity extends AppCompatActivity implements HomeView{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.homeRoot, GenerateRecipeFragment.newInstance(this))
                    .commit();

    }

    @Override
    public void addTimerFragment(DiceUI diceUI) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homeRoot, TimerFragment.newInstance(diceUI))
                .addToBackStack(null)
                .commit();
    }
}
