package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
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
    public void addRecipeToFavourites(OnSaveRecipeToDBFinishListener listener, Context ctx) {

    }

    @Override
    public void setIsHistoryEmptyBool(Context ctx, boolean bool) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isHistoryEmpty", bool);
        editor.apply();
    }
}
