package aeropresscipe.divinelink.aeropress.history;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;

interface IHistoryView {

    void showHistory(List<HistoryDomain> savedRecipes);

    void passData(int bloomTime, int brewTime, int bloomWater, int brewWater);

    void showEmptyListMessage();

    void setRecipeLiked(boolean isLiked, Integer pos);
}
