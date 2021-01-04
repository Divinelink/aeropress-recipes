package aeropresscipe.divinelink.aeropress.timer;


import android.content.Context;

public interface TimerPresenter {

    void startBrewing(int bloomAndBrewTime, boolean isBloom, Context ctx);

    void showMessage();

    void returnValuesOnResume(Context ctx);

    void saveValuesOnPause(Context ctx, int timeTimeLeftInMillis, int brewTime, boolean timerRunning);

    void saveLikedRecipeOnDB(Context ctx);
}
