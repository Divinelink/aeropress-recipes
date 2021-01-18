package aeropresscipe.divinelink.aeropress.timer;

import android.content.Context;


public interface TimerInteractor {


    void saveValues(OnStartTimerFinishListener listener, Context ctx, int bloomTime, int brewTime, boolean isBloomTimer);

    void returnValues(OnStartTimerFinishListener listener, Context ctx);

    void saveLikedRecipe(OnSaveLikedRecipeFinishListener listener, Context ctx);

    void checkIfRecipeIsLikedAndSavedOnDB(OnSaveLikedRecipeFinishListener listener, Context ctx);

    void addRecipeToHistory(Context ctx);

    interface OnStartTimerFinishListener{
        void onSuccess(int time, boolean bloomPhase);
        void onBrewFinished();
    }

    interface OnSaveLikedRecipeFinishListener{
        void onSavedRecipe(boolean isSaved);
        void onError();
    }
}
