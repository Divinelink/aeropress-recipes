package aeropresscipe.divinelink.aeropress.savedrecipes;


import java.util.List;

interface SavedRecipesView {

    void showSavedRecipes(List<SavedRecipeDomain> savedRecipes);

    void passData(int bloomTime, int brewTime, int bloomWater, int brewWater);

    void showSavedRecipesAfterDeletion(List<SavedRecipeDomain> savedRecipes);

    void showEmptyListMessage();
}
