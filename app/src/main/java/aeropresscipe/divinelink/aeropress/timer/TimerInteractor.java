package aeropresscipe.divinelink.aeropress.timer;

import android.content.Context;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;

public interface TimerInteractor {

    void startTimer(OnStartTimerFinishListener listener, int bloomTime, boolean bloomPhase);



    interface OnStartTimerFinishListener{
        void onSuccess(int bloomTime, boolean bloomPhase);
        void onError();
    }




}
