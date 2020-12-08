package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;


public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener, Context ctx);

    void getNewRecipe(OnGenerateRecipeFinishListener listener, Context ctx, boolean letGenerate);

    interface OnGenerateRecipeFinishListener{

        void onSuccess(DiceDomain randomRecipe);

        void onSuccessNewRecipe(DiceDomain randomRecipe);

        void onSuccessAppStarts(DiceDomain randomRecipe);

        void isAlreadyBrewing();
    }
}
