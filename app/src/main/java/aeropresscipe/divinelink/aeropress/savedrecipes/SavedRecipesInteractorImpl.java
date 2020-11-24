package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.features.SharedPreferencesListManager;
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
    public void deleteRecipeFromDB(final OnGetRestFavouritesAfterDeletionFinishListener listener, final SavedRecipeDomain recipeDomain, final Context ctx, final int position) {

        final SharedPreferencesListManager prefManagerList = new SharedPreferencesListManager();
        final ArrayList<Integer> openPositions = prefManagerList.getArrayList("openPositions", ctx);

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


                //We remove the object from the openPositions list and then we store the updated list and pass it back to the swipe helper.
                //This object was the one we deleted from the favourites, using the swipe feature.
                Collections.sort(openPositions);
                int indexOfRemovedItem = openPositions.indexOf(position);
                openPositions.remove(indexOfRemovedItem);

                prefManagerList.saveArrayList(decrementOpenPositions(openPositions, indexOfRemovedItem), "openPositions", ctx);

                listener.onSuccessAfterDeletion(myData, position);
            }

        });
    }

    private ArrayList<Integer> decrementOpenPositions(ArrayList<Integer> mArrayList, int index) {



        for (int i = index ; i <= mArrayList.size()-1; i++) {
            Integer value = mArrayList.get(i)-1;
            mArrayList.set(i, value);
        }
        return mArrayList;
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


