package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;


public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener, Context ctx);

    void getNewRecipe(OnGenerateRecipeFinishListener listener, Context ctx, boolean letGenerate);

    interface OnGenerateRecipeFinishListener{

        void onSuccess(int temp,
                       String groundSize,
                       int brewTime,
                       String brewingMethod,
                       int bloomTime,
                       int bloomWater,
                       int waterAmount,
                       int coffeeAmount);

        void onSuccessNewRecipe(int temp,
                                String groundSize,
                                int brewTime,
                                String brewingMethod,
                                int bloomTime,
                                int bloomWater,
                                int waterAmount,
                                int coffeeAmount);

        void isAlreadyBrewing();
    }
}
