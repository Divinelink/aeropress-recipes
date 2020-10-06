package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.content.Context;

import java.util.ArrayList;

public class GenerateRecipePresenterImpl implements GenerateRecipePresenter, GenerateRecipeInteractor.OnGenerateRecipeFinishListener{

    private GenerateRecipeView generateRecipeView;
    private GenerateRecipeInteractor interactor;



    public GenerateRecipePresenterImpl(GenerateRecipeView generateRecipeView) {
        this.generateRecipeView = generateRecipeView;
        interactor = new GenerateRecipeInteractorImpl();
    }

   /* @Override
    public void onSuccess(ArrayList<DiceDomain> tempDice,
                          ArrayList<DiceDomain> groundSizeDice,
                          ArrayList<DiceDomain> brewingMethodDice,
                          ArrayList<DiceDomain> waterAmountDice) {

        generateRecipeView.showRecipe(tempDice, groundSizeDice, brewingMethodDice, waterAmountDice);


    }*/

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
    }

    @Override
    public void onError() {

    }

    @Override
    public void getRecipe() {
        interactor.getRecipe(this);
    }

    @Override
    public void startTimer(Context ctx, int bloomTime, int brewTime) {

    }
}
