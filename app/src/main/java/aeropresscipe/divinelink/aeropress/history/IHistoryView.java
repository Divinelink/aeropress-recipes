package aeropresscipe.divinelink.aeropress.history;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;

interface IHistoryView {

    void showHistory(List<DiceDomain> savedRecipes);

    void showEmptyListMessage();
}
