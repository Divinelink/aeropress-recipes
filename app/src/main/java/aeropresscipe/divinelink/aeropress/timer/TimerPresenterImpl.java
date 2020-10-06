package aeropresscipe.divinelink.aeropress.timer;

import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeInteractor;

public class TimerPresenterImpl implements TimerPresenter, TimerInteractor.OnStartTimerFinishListener {

    private TimerView timerView;
    private TimerInteractor interactor;

    public TimerPresenterImpl(TimerView timerView) {
        this.timerView = timerView;
        interactor = new TimerInteractorImpl();
    }

    @Override
    public void onSuccess() {
        timerView.showTimer();
    }

    @Override
    public void onError() {

    }

    @Override
    public void getNumbersForTimer() {
        interactor.startTimer(this);
    }
}
