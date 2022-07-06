package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe;

interface IHistoryInteractor {


    void getHistoryFromDB(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void deleteHistory(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void getSpecificRecipeFromDB(OnGetHistoryFromDBFinishListener listener, Context ctx, int position);

    void addRecipeToFavourites(OnSaveRecipeToDBFinishListener listener, Context ctx, int pos, Recipe recipe);

    interface OnGetHistoryFromDBFinishListener{

        void onSuccess(List<History> historyRecipes);
        void onSuccessSingleRecipe(History history);
        void onEmptyList();
        void onError();

    }

    interface OnSaveRecipeToDBFinishListener{
        void onSaveRecipe(boolean isSaved, int pos);
    }
}
