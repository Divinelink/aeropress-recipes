package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

interface SavedRecipesPresenter {

    void getSavedRecipes(Context ctx);

    void startBrew(Context ctx, SavedRecipeDomain recipe);

    void deleteRecipe(Context ctx, SavedRecipeDomain recipe);

}
