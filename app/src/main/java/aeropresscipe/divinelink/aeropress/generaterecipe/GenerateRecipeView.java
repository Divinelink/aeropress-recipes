package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

public interface GenerateRecipeView {

   /* void showRecipe(ArrayList<DiceDomain> tempDice,
                    ArrayList<DiceDomain> groundSizeDice,
                    ArrayList<DiceDomain> brewingMethodDice,
                    ArrayList<DiceDomain> waterAmountDice);
    */

    void showRecipe(int temp, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int waterAmount, int coffeeAmount);


}
