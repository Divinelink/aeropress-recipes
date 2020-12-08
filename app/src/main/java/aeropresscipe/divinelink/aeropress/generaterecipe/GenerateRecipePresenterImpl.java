package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;

public class GenerateRecipePresenterImpl implements GenerateRecipePresenter, GenerateRecipeInteractor.OnGenerateRecipeFinishListener {

    private final GenerateRecipeView generateRecipeView;
    private final GenerateRecipeInteractor interactor;


    public GenerateRecipePresenterImpl(GenerateRecipeView generateRecipeView) {
        this.generateRecipeView = generateRecipeView;
        interactor = new GenerateRecipeInteractorImpl();
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

    @Override
    public void onSuccessAppStarts(DiceDomain randomRecipe) {
        generateRecipeView.showRecipeAppStarts(randomRecipe);

        generateRecipeView.passData(randomRecipe.getBloomTime(),
                randomRecipe.getBrewTime(),
                randomRecipe.getBloomWater(),
                randomRecipe.getBrewWaterAmount() - randomRecipe.getBloomWater());
    }


    @Override
    public void onSuccessNewRecipe(DiceDomain randomRecipe) {
        generateRecipeView.showRecipeRemoveResume(randomRecipe);

        generateRecipeView.passData(randomRecipe.getBloomTime(),
                randomRecipe.getBrewTime(),
                randomRecipe.getBloomWater(),
                randomRecipe.getBrewWaterAmount() - randomRecipe.getBloomWater());
    }

    @Override
    public void onSuccess(DiceDomain randomRecipe) {
        generateRecipeView.showRecipe(randomRecipe);

        generateRecipeView.passData(randomRecipe.getBloomTime(),
                randomRecipe.getBrewTime(),
                randomRecipe.getBloomWater(),
                randomRecipe.getBrewWaterAmount() - randomRecipe.getBloomWater());
    }

}
