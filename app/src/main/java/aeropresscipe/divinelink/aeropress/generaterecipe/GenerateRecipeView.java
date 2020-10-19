package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

public interface GenerateRecipeView {

    void showRecipe(int temp, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int waterAmount, int coffeeAmount);

    void showRecipeRemoveResume(int temp, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int waterAmount, int coffeeAmount);

    void passData(int bloomTime, int brewTime, int bloomWater, int remainingBrewWater);

    void showIsAlreadyBrewingDialog();

}
