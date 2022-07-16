package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe;

public class HistoryPresenterImpl implements IHistoryPresenter, IHistoryInteractor.OnGetHistoryFromDBFinishListener, IHistoryInteractor.OnSaveRecipeToDBFinishListener {

    final private IHistoryView historyView;
    final private HistoryInteractorImpl interactor;

    public HistoryPresenterImpl(IHistoryView historyView) {
        this.historyView = historyView;
        interactor = new HistoryInteractorImpl();
    }

    @Override
    public void onSuccess(List<History> historyRecipes) {
        historyView.showHistory(historyRecipes);
    }

    @Override
    public void onSuccessSingleRecipe(History history) {
        historyView.passData(history.getRecipe());
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
    public void addRecipeToFavourites(Context ctx, int pos, Recipe recipe) {
        interactor.addRecipeToFavourites(this, ctx, pos, recipe);
    }

    @Override
    public void onSaveRecipe(boolean isSaved, int pos) {
        historyView.setRecipeLiked(isSaved, pos);
    }
}
