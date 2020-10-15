package aeropresscipe.divinelink.aeropress.timer;

import android.content.Context;

import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeInteractor;

public class TimerPresenterImpl implements TimerPresenter, TimerInteractor.OnStartTimerFinishListener {

    private TimerView timerView;
    private TimerInteractor interactor;

    public TimerPresenterImpl(TimerView timerView) {
        this.timerView = timerView;
        interactor = new TimerInteractorImpl();
    }

    @Override
    public void returnValuesOnResume(Context ctx) {

        // Get values from the model when resuming to TimerFragment.
        // And return them on the view.
        //  interactor.startTimer(this,  ctx);
        //  interactor.startTimer(this, ctx, );
        interactor.returnValues(this, ctx);

    }

    @Override
    public void saveValuesOnPause(Context ctx, int time, boolean timer) {

        interactor.saveValues(this, ctx, time, timer);

    }

    @Override
    public void showMessage(String message) {
        timerView.showMessage(message);
    }

    @Override
    public void getNumbersForTimer(int time, boolean bloomPhase) {
        timerView.showTimer(time, bloomPhase);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onSuccess(int time, boolean bloomPhase) {

        timerView.showTimer(time, bloomPhase);

    }


}
