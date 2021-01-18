package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;
import aeropresscipe.divinelink.aeropress.timer.TimerInteractorImpl;

public class HistoryInteractorImpl implements IHistoryInteractor, ISharedPrefHistoryManager {


    @Override
    public void getHistoryFromDB(final OnGetHistoryFromDBFinishListener listener, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final HistoryDao recipeDao = HomeDatabase.getDatabase(ctx).historyDao();
                List<HistoryDomain> mHistoryRecipes = recipeDao.getHistoryRecipes();

                if (mHistoryRecipes.size() >= 10) {
                    recipeDao.deleteSurplus();
                    mHistoryRecipes = recipeDao.getHistoryRecipes();
                }

                if (mHistoryRecipes.size() != 0) {
                    setIsHistoryEmptyBool(ctx, false);
                    listener.onSuccess(mHistoryRecipes);
                } else {
                    setIsHistoryEmptyBool(ctx, true);
                    listener.onEmptyList();
                }
            }
        });
    }

    @Override
    public void getSpecificRecipeFromDB(final OnGetHistoryFromDBFinishListener listener, final Context ctx, final int position) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                final HistoryDao historyDao = HomeDatabase.getDatabase(ctx).historyDao();
                final List<HistoryDomain> mSavedRecipes = historyDao.getHistoryRecipes();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        recipeDao.updateRecipe(mSavedRecipes.get(position).getDiceDomain());
                    }
                });

                listener.onSuccessSingleRecipe(mSavedRecipes.get(position));

            }
        });
    }

    @Override
    public void deleteHistory(final OnGetHistoryFromDBFinishListener listener, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final HistoryDao historyDao = HomeDatabase.getDatabase(ctx).historyDao();
                final List<HistoryDomain> mSavedRecipes = historyDao.getHistoryRecipes();

                if (mSavedRecipes.size() != 0) {
                    historyDao.deleteAll();
                    setIsHistoryEmptyBool(ctx, true);
                    listener.onEmptyList();
                } else {
                    listener.onError();
                }

            }
        });
    }

    @Override
    public void addRecipeToFavourites(final OnSaveRecipeToDBFinishListener listener, final Context ctx, final int pos, final int id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();
                final HistoryDao historyDao = HomeDatabase.getDatabase(ctx).historyDao();

                final DiceDomain recipe = historyDao.getSpecificRecipe(id);
                final boolean recipeExists = savedRecipeDao.recipeExists(id);

                if (!recipeExists) {
                    historyDao.updateRecipe(new HistoryDomain(recipe, getCurrentDate(), true));
                    savedRecipeDao.insertLikedRecipe(new SavedRecipeDomain(recipe, getCurrentDate()));
                    listener.onSaveRecipe(true, pos);
                } else {
                    historyDao.updateRecipe(new HistoryDomain(recipe, getCurrentDate(), false));
                    savedRecipeDao.delete(new SavedRecipeDomain(recipe, getCurrentDate()));
                    listener.onSaveRecipe(false, pos);
                }
            }
        });
    }

    @Override
    public void setIsHistoryEmptyBool(Context ctx, boolean bool) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isHistoryEmpty", bool);
        editor.apply();
    }

    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy");

        return formatter.format(date.getTime());
    }
}
