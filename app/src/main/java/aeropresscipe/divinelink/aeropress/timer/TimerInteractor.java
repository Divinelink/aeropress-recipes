package aeropresscipe.divinelink.aeropress.timer;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;

public interface TimerInteractor {

    void startTimer(OnStartTimerFinishListener listener);

    interface OnStartTimerFinishListener{
        void onSuccess();
        void onError();
    }




}
