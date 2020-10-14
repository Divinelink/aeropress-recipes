package aeropresscipe.divinelink.aeropress.timer;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TimerInteractorImpl implements TimerInteractor {


    @Override
    public void saveValues(OnStartTimerFinishListener listener, Context ctx, int start_time_in_millis) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();

        int mTimeLeftInMillis = preferences.getInt("millisLeft", start_time_in_millis);
        boolean mTimerRunning = preferences.getBoolean("timerRunning", false);

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.apply();

        //    listener.onSuccess(mTimeLeftInMillis, mTimerRunning);

    }




    @Override
    public void returnValues(OnStartTimerFinishListener listener, Context ctx) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);


        //FIXME figure out how to get start_time_in_millis and timerRunning
        int mTimeLeftInMillis = preferences.getInt("millisLeft", start_time_in_millis);
        boolean mTimerRunning = preferences.getBoolean("timerRunning", timerRunning);
        listener.onSuccess(mTimeLeftInMillis, mTimerRunning);
    }
}
