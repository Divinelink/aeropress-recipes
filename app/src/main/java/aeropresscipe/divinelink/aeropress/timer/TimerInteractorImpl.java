package aeropresscipe.divinelink.aeropress.timer;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TimerInteractorImpl implements TimerInteractor {


    @Override
    public void saveValues(OnStartTimerFinishListener listener, Context ctx, int start_time_in_millis, boolean mTimerRunning) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();

        long endTime = System.currentTimeMillis() + start_time_in_millis * 1000;

        editor.putLong("endTime", endTime);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.apply();
    }


    @Override
    public void returnValues(OnStartTimerFinishListener listener, Context ctx) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);

        long mTimeLeftInMillis = preferences.getLong("endTime", 0);
        boolean mTimerRunning = preferences.getBoolean("timerRunning", true);

        long minutes = (mTimeLeftInMillis - System.currentTimeMillis()) / 1000 / 60;
        long seconds = (mTimeLeftInMillis - System.currentTimeMillis()) / 1000 % 60;

        int endTime = (int) minutes * 60 + (int) seconds;

        listener.onSuccess(endTime, mTimerRunning);
    }
}
