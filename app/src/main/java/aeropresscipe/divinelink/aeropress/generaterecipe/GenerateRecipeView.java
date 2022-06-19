package aeropresscipe.divinelink.aeropress.generaterecipe;


public interface GenerateRecipeView {

    void showRecipe(DiceDomain randomRecipe);

    void showRecipeAppStarts(DiceDomain randomRecipe);

    void showRecipeRemoveResume(DiceDomain randomRecipe);

    void passData(DiceDomain dice);

    void showIsAlreadyBrewingDialog();

}
