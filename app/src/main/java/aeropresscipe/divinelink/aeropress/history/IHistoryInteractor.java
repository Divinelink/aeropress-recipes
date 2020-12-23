package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;

interface IHistoryInteractor {


    void getHistoryFromDB(OnGetHistoryFromDBFinishListener listener, Context ctx);

    void deleteHistory(OnGetHistoryFromDBFinishListener listener, Context ctx);


    interface OnGetHistoryFromDBFinishListener{

        void onSuccess(List<DiceDomain> historyRecipes);
        void onEmptyList();
        void onError();

    }

}
