package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.features.SharedPreferencesListManager;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import aeropresscipe.divinelink.aeropress.history.HistoryDao;
import aeropresscipe.divinelink.aeropress.history.HistoryDomain;
import androidx.annotation.RequiresApi;

public class SavedRecipesInteractorImpl implements SavedRecipesInteractor {


    @Override
    public void getListsFromDB(final OnGetSavedListsFromDBFinishListener listener, final Context ctx) {


        AsyncTask.execute(() -> {
            final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
            final List<SavedRecipeDomain> mSavedRecipes = savedRecipeDao.getSavedRecipes();

            if (mSavedRecipes.size() != 0)
                listener.onSuccess(mSavedRecipes);
            else
                listener.onEmptyList();
        });

    }

    @Override
    public void deleteRecipeFromDB(final OnGetRestFavouritesAfterDeletionFinishListener listener, final SavedRecipeDomain recipeDomain, final Context ctx) {
        AsyncTask.execute(() -> {
            final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
            final HistoryDao historyDao = HomeDatabase.getDatabase(ctx).historyDao();

            savedRecipeDao.delete(recipeDomain);
            //Updates history DB, turn corresponding entry's Like Button off.
            if (historyDao.historyRecipeExists(recipeDomain.getId())) {
                historyDao.updateRecipe(new HistoryDomain(recipeDomain.getDiceDomain(), recipeDomain.getDateBrewed(), false));
            }
            final List<SavedRecipeDomain> mSavedRecipes = savedRecipeDao.getSavedRecipes();


            //We remove the object from the openPositions list and then we store the updated list and pass it back to the swipe helper.
            //This object was the one we deleted from the favourites, using the swipe feature.


            if (mSavedRecipes.size() != 0) {
                Log.d("Recipes Interactor", "Successfully deleted recipe: " + recipeDomain);
                listener.onSuccessAfterDeletion(mSavedRecipes);
            }
            else {
                listener.onEmptyList();
            }
        });
    }

    private ArrayList<Integer> decrementOpenPositions(ArrayList<Integer> mArrayList, int index) {

        for (int i = index; i <= mArrayList.size() - 1; i++) {
            Integer value = mArrayList.get(i) - 1;
            mArrayList.set(i, value);
        }
        return mArrayList;
    }


    @Override
    public void getSpecificRecipeFromDB(final OnGetSingleRecipeFromDBFinishListener listener, final Context ctx, final int position) {
        AsyncTask.execute(() -> {

            final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
            final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
            final List<SavedRecipeDomain> mSavedRecipes = savedRecipeDao.getSavedRecipes();

            // final SavedRecipeDomain currentRecipe = savedRecipeDao.getSingleRecipe(position);
            // FIXME find a way to get only a single SavedRecipeDomain from query


            //We also have to update the current recipe, so when we start the timer, the current recipe will be displayed.
            // Also, when we go back to the starting fragment, the displayed recipe will be the one we select from the favourites.
            Log.d("getSpecificRecipeFromDB", "Updates the current recipe to the one we selected to brew from favourites.");
            AsyncTask.execute(() -> recipeDao.updateRecipe(mSavedRecipes.get(position).getDiceDomain()));
            listener.onSuccessGetSingleRecipe(mSavedRecipes.get(position));
        });

    }

}


