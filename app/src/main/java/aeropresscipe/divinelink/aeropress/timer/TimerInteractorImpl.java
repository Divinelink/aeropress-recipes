package aeropresscipe.divinelink.aeropress.timer;

public class TimerInteractorImpl implements TimerInteractor{


    @Override
    public void startTimer(OnStartTimerFinishListener listener) {


        listener.onSuccess();
    }
}
