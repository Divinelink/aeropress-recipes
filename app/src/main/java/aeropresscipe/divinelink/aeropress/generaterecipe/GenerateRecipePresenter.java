package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;


public interface GenerateRecipePresenter {

    void getRecipe(Context ctx);

    void getNewRecipe(Context ctx, boolean letGenerate);

}
