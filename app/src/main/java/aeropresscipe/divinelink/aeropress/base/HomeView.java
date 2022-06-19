package aeropresscipe.divinelink.aeropress.base;


import java.io.Serializable;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;

public interface HomeView extends Serializable {

    void addSavedRecipesFragment();

    void startTimerActivity(DiceDomain dice);

    void addGenerateRecipeFragment();

    void addHistoryFragment();

}
