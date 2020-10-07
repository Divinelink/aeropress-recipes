package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

public interface GenerateRecipeView {

    void showRecipe(int temp, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int waterAmount, int coffeeAmount);

    void passData(int bloomTime, int brewTime);
    //FIXME pass data on passdata instead of showrecipe in fragment

}
