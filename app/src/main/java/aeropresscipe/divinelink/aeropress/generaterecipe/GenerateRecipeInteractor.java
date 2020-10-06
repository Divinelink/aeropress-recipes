package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener);

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
