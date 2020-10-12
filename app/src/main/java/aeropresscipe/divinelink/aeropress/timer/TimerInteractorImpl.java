package aeropresscipe.divinelink.aeropress.timer;


public class TimerInteractorImpl implements TimerInteractor{


    @Override
    public void startTimer(OnStartTimerFinishListener listener, int time, boolean bloomPhase) {

        listener.onSuccess(time, bloomPhase);

    }

}
