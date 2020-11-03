package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import androidx.annotation.RequiresApi;

public class SavedRecipesInteractorImpl implements SavedRecipesInteractor {


    @Override
    public void getListsFromDB(final OnGetSavedListsFromDBFinishListener listener, final Context ctx) {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();

                listener.onSuccess(myData);
            }

        });

    }

    @Override
    public void deleteRecipeFromDB(final OnGetSavedListsFromDBFinishListener listener, final SavedRecipeDomain recipeDomain, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();

                savedRecipeDao.deleteCurrent(recipeDomain.getDiceTemperature(),
                        recipeDomain.getGroundSize(),
                        recipeDomain.getBrewTime(),
                        recipeDomain.getBrewingMethod(),
                        recipeDomain.getBloomTime(),
                        recipeDomain.getBloomWater(),
                        recipeDomain.getBrewWaterAmount(),
                        recipeDomain.getCoffeeAmount());

                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();
                listener.onSuccess(myData);
            }

        });
    }


    @Override
    public void getSpecificRecipeFromDB(final OnGetSingleRecipeFromDBFinishListener listener, final Context ctx, final int position) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();

                //We also have to update the current recipe, so when we start the timer, the current recipe will be displayed.
                // Also, when we go back to the starting fragment, the displayed recipe will be the one we select from the favourites.
                final DiceDomain currentRecipe = new DiceDomain(
                        myData.get(position).getDiceTemperature(),
                        myData.get(position).getGroundSize(),
                        myData.get(position).getBrewTime(),
                        myData.get(position).getBrewingMethod(),
                        myData.get(position).getBloomTime(),
                        myData.get(position).getBloomWater(),
                        myData.get(position).getBrewWaterAmount(),
                        myData.get(position).getCoffeeAmount());

                Log.d("getSpecificRecipeFromDB", "Updates the current recipe to the one we selected to brew from favourites.");
                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        recipeDao.updateRecipe(currentRecipe);
                    }
                });
                listener.onSuccessGetSingleRecipe(myData.get(position));
            }

        });

    }
}
