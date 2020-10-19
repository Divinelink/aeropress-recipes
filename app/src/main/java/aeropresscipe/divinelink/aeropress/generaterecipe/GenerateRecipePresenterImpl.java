package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;

public class GenerateRecipePresenterImpl implements GenerateRecipePresenter, GenerateRecipeInteractor.OnGenerateRecipeFinishListener {

    private GenerateRecipeView generateRecipeView;
    private GenerateRecipeInteractor interactor;


    public GenerateRecipePresenterImpl(GenerateRecipeView generateRecipeView) {
        this.generateRecipeView = generateRecipeView;
        interactor = new GenerateRecipeInteractorImpl();
    }

    @Override
    public void onSuccessNewRecipe(int temp, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int waterAmount, int coffeeAmount) {

        generateRecipeView.showRecipeRemoveResume(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);
        generateRecipeView.passData(bloomTime, brewTime, bloomWater, waterAmount - bloomWater);

    }

    @Override
    public void onSuccess(int temp,
                          String groundSize,
                          int brewTime,
                          String brewingMethod,
                          int bloomTime,
                          int bloomWater,
                          int waterAmount,
                          int coffeeAmount) {

        generateRecipeView.showRecipe(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);
        generateRecipeView.passData(bloomTime, brewTime, bloomWater, waterAmount - bloomWater);

    }

    @Override
    public void isAlreadyBrewing() {

        generateRecipeView.showIsAlreadyBrewingDialog();

    }

    @Override
    public void getNewRecipe(Context ctx, boolean letGenerate) {
        interactor.getNewRecipe(this, ctx, letGenerate);
    }

    @Override
    public void getRecipe(Context ctx) {
        interactor.getRecipe(this, ctx);
    }


}
