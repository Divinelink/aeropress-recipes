package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

public interface GenerateRecipeInteractor {

    void getRecipe(OnGenerateRecipeFinishListener listener);

    interface OnGenerateRecipeFinishListener{

        void onSuccess(ArrayList<DiceDomain> tempDice,
                       ArrayList<DiceDomain> groundSizeDice,
                       ArrayList<DiceDomain> brewingMethodDice,
                       ArrayList<DiceDomain> waterAmountDice);

        void onError();
    }
}
