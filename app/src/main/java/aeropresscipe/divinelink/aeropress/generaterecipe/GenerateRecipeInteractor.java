package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;


public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener, Context ctx);

    void getNewRecipe(OnGenerateRecipeFinishListener listener, Context ctx, boolean letGenerate);

    interface OnGenerateRecipeFinishListener{

        void onSuccess(Recipe randomRecipe);

        void onSuccessNewRecipe(Recipe randomRecipe);

        void onSuccessAppStarts(Recipe randomRecipe);

        void isAlreadyBrewing();
    }
}
