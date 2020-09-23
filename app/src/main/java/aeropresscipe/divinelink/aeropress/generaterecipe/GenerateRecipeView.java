package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

public interface GenerateRecipeView {

    void showRecipe(ArrayList<DiceDomain> tempDice,
                    ArrayList<DiceDomain> groundSizeDice,
                    ArrayList<DiceDomain> brewingMethodDice,
                    ArrayList<DiceDomain> waterAmountDice);

}
