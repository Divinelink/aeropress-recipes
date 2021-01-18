package aeropresscipe.divinelink.aeropress.timer;

import android.content.Context;

public class TimerPresenterImpl implements TimerPresenter, TimerInteractor.OnStartTimerFinishListener, TimerInteractor.OnSaveLikedRecipeFinishListener {

    private TimerView timerView;
    private TimerInteractor interactor;

    public TimerPresenterImpl(TimerView timerView) {
        this.timerView = timerView;
        interactor = new TimerInteractorImpl();
    }

    @Override
    public void saveValuesOnPause(Context ctx, int timeTimeLeftInMillis, int brewTime, boolean timerRunning) {
        interactor.saveValues(this, ctx, timeTimeLeftInMillis, brewTime, timerRunning);
    }

    @Override
    public void returnValuesOnResume(Context ctx) {

        // Get values from the model when resuming to TimerFragment.
        // And return them on the view.
        interactor.returnValues(this, ctx);
        interactor.checkIfRecipeIsLikedAndSavedOnDB(this, ctx);

    }

    @Override
    public void showMessage() {
        timerView.showMessage();
    }

    @Override
    public void startBrewing(int time, boolean bloomPhase, Context ctx) {
        timerView.showTimer(time, bloomPhase);
        interactor.checkIfRecipeIsLikedAndSavedOnDB(this, ctx);
        interactor.addRecipeToHistory(ctx);
    }

    @Override
    public void onBrewFinished() {

        timerView.showMessage();
    }

    @Override
    public void onSuccess(int time, boolean bloomPhase) {
        timerView.showTimer(time, bloomPhase);
    }

    @Override
    public void saveLikedRecipeOnDB(Context ctx) {
        interactor.saveLikedRecipe(this, ctx);
    }

    @Override
    public void onSavedRecipe(boolean isSaved) {
        timerView.addToLiked(isSaved);
    }

    @Override
    public void onError() {

    }
}
