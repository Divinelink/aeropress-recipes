package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;

public interface GenerateRecipePresenter {

    void getRecipe();

    void startTimer(Context ctx, int bloomTime, int brewTime);

}
