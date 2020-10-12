package aeropresscipe.divinelink.aeropress.timer;


public class TimerInteractorImpl implements TimerInteractor{


    @Override
    public void startTimer(OnStartTimerFinishListener listener, int bloomTime, boolean bloomPhase) {

        listener.onSuccess(bloomTime, bloomPhase);

    }

}
