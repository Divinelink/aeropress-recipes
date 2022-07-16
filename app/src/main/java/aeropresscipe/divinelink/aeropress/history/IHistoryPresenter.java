package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe;

interface IHistoryPresenter {

    void getHistoryRecipes(Context ctx);

    void getSpecificRecipeToStartNewBrew(Context ctx, int position);

    void clearHistory(Context ctx);

    void addRecipeToFavourites(Context ctx, int pos, Recipe recipe);

}
