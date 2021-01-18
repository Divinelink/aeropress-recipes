package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

interface IHistoryInteractor {


    void getHistoryFromDB(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void deleteHistory(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void getSpecificRecipeFromDB(OnGetHistoryFromDBFinishListener listener, Context ctx, int position);

    void addRecipeToFavourites(OnSaveRecipeToDBFinishListener listener, Context ctx, int pos, int id);

    interface OnGetHistoryFromDBFinishListener{

        void onSuccess(List<HistoryDomain> historyRecipes);
        void onSuccessSingleRecipe(HistoryDomain historyDomain);
        void onEmptyList();
        void onError();

    }

    interface OnSaveRecipeToDBFinishListener{
        void onSaveRecipe(boolean isSaved, int pos);
    }



}
