package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;

public class HistoryPresenterImpl implements IHistoryPresenter, IHistoryInteractor.OnGetHistoryFromDBFinishListener {

    final private IHistoryView historyView;
    final private HistoryInteractorImpl interactor;

    public HistoryPresenterImpl(IHistoryView historyView) {
        this.historyView = historyView;
        interactor = new HistoryInteractorImpl();
    }

    @Override
    public void onSuccess(List<HistoryDomain> historyRecipes) {
            historyView.showHistory(historyRecipes);
    }

    @Override
    public void onEmptyList() {
        historyView.showEmptyListMessage();
    }

    @Override
    public void onError() {

    }

    @Override
    public void getHistoryRecipes(Context ctx) {
        interactor.getHistoryFromDB(this, ctx);
    }

    @Override
    public void getSpecificRecipeToStartNewBrew(Context ctx, int position) {

    }

    @Override
    public void clearHistory(Context ctx) {

        interactor.deleteHistory(this, ctx);

    }
}
