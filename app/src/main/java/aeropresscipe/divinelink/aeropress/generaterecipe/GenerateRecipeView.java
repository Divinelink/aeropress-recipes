package aeropresscipe.divinelink.aeropress.generaterecipe;


public interface GenerateRecipeView {

    void showRecipe(Recipe randomRecipe);

    void showRecipeAppStarts(Recipe randomRecipe);

    void showRecipeRemoveResume(Recipe randomRecipe);

    void passData(Recipe dice);

    void showIsAlreadyBrewingDialog();

}
