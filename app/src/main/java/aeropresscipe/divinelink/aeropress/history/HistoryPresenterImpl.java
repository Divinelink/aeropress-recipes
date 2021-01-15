package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

import aeropresscipe.divinelink.aeropress.timer.TimerInteractor;
import aeropresscipe.divinelink.aeropress.timer.TimerInteractorImpl;

public class HistoryPresenterImpl implements IHistoryPresenter, IHistoryInteractor.OnGetHistoryFromDBFinishListener, IHistoryInteractor.OnSaveRecipeToDBFinishListener, TimerInteractor.OnSaveLikedRecipeFinishListener  {

    final private IHistoryView historyView;
    final private HistoryInteractorImpl interactor;
    final private TimerInteractor timerInteractor;

    public HistoryPresenterImpl(IHistoryView historyView) {
        this.historyView = historyView;
        interactor = new HistoryInteractorImpl();
        timerInteractor = new TimerInteractorImpl();
    }

    @Override
    public void onSuccess(List<HistoryDomain> historyRecipes) {
        historyView.showHistory(historyRecipes);
    }

    @Override
    public void onSuccessSingleRecipe(HistoryDomain historyDomain) {

        historyView.passData(historyDomain.getBloomTime(),
                historyDomain.getBrewTime(),
                historyDomain.getBloomWater(),
                historyDomain.getBrewWaterAmount() - historyDomain.getBloomWater());
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
        interactor.getSpecificRecipeFromDB(this, ctx, position);
    }

    @Override
    public void clearHistory(Context ctx) {
        interactor.deleteHistory(this, ctx);
    }

    @Override
    public void addRecipeToFavourites(Context ctx, int pos) {
        timerInteractor.saveLikedRecipe(this, ctx);
    }

    @Override
    public void onSaveRecipe() {

    }

    @Override
    public void onSavedRecipe(boolean isSaved) {
        historyView.setRecipeLiked(isSaved);
    }
}
