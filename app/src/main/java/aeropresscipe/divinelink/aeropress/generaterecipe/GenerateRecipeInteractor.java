package aeropresscipe.divinelink.aeropress.generaterecipe;

public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener);

    interface OnGenerateRecipeFinishListener{

        void onSuccess(int waterTemp, String groundSize, String brewingMethod, int waterAmount);

        void onError();
    }
}
