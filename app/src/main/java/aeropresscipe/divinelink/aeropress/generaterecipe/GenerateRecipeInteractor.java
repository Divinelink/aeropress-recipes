package aeropresscipe.divinelink.aeropress.generaterecipe;

public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener);

    interface OnGenerateRecipeFinishListener{

        void onSuccess();

        void onError();
    }
}
