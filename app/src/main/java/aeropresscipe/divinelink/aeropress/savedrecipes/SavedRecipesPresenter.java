package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

interface SavedRecipesPresenter {

    void getSavedRecipes(Context ctx);

    void getSpecificRecipeToStartNewBrew(Context ctx, int position);

    void deleteRecipe(SavedRecipeDomain savedRecipeDomain, Context ctx);

}
