package aeropresscipe.divinelink.aeropress.base;


import java.io.Serializable;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe;

public interface HomeView extends Serializable {

    void addSavedRecipesFragment();

    void startTimerActivity(Recipe dice);

    void addGenerateRecipeFragment();

    void addHistoryFragment();

}
