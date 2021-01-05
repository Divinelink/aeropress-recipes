package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

interface IHistoryInteractor {


    void getHistoryFromDB(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void deleteHistory(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void getSpecificRecipeFromDB(IHistoryInteractor.OnGetHistoryFromDBFinishListener listener, Context ctx, int position);


    interface OnGetHistoryFromDBFinishListener{

        void onSuccess(List<HistoryDomain> historyRecipes);
        void onSuccessSingleRecipe(HistoryDomain historyDomain);
        void onEmptyList();
        void onError();

    }

}
