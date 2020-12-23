package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;

public class HistoryInteractorImpl implements IHistoryInteractor {


    @Override
    public void getHistoryFromDB(final OnGetHistoryFromDBFinishListener listener, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                final List<DiceDomain> mHistoryRecipes = recipeDao.getRecipeHistory();


                if (mHistoryRecipes.size() != 0)
                    listener.onSuccess(mHistoryRecipes);
                else
                    listener.onEmptyList();
            }

        });

    }

    @Override
    public void deleteHistory(OnGetHistoryFromDBFinishListener listener, Context ctx) {

    }
}
