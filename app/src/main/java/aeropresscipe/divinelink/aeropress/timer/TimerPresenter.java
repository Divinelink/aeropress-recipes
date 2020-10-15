package aeropresscipe.divinelink.aeropress.timer;


import android.content.Context;

public interface TimerPresenter {

    void getNumbersForTimer(int bloomAndBrewTime, boolean isBloom);

    void showMessage(String message);

    void returnValuesOnResume(Context ctx);

    void saveValuesOnPause(Context ctx, int timeTimeLeftInMillis, boolean timerRunning);
}
