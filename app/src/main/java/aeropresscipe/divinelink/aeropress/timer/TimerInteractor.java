package aeropresscipe.divinelink.aeropress.timer;

import android.content.Context;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;

public interface TimerInteractor {

  //  void startTimer(OnStartTimerFinishListener listener, Context ctx, long START_TIME_IN_MILLIS);

    void saveValues(OnStartTimerFinishListener listener, Context ctx, int START_TIME_IN_MILLIS);

    void returnValues(OnStartTimerFinishListener listener, Context ctx);

    interface OnStartTimerFinishListener{
        void onSuccess(int time, boolean bloomPhase);
        void onError();
    }




}
