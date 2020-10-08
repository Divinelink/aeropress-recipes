package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;

import java.util.ArrayList;

public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener, Context ctx);

    void getNewRecipe(OnGenerateRecipeFinishListener listener, Context ctx);

    interface OnGenerateRecipeFinishListener{

        void onSuccess(int temp,
                       String groundSize,
                       int brewTime,
                       String brewingMethod,
                       int bloomTime,
                       int bloomWater,
                       int waterAmount,
                       int coffeeAmount);

        void onError();
    }
}
