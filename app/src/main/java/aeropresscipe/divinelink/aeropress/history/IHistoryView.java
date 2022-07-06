package aeropresscipe.divinelink.aeropress.history;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe;

interface IHistoryView {

    void showHistory(List<History> savedRecipes);

    void passData(Recipe recipe);

    void showEmptyListMessage();

    void setRecipeLiked(boolean isLiked, Integer pos);
}
