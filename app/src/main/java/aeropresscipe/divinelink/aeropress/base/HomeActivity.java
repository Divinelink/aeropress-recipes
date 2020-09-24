package aeropresscipe.divinelink.aeropress.base;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.homeRoot, GenerateRecipeFragment.newInstance())
                .commit();
    }
}
