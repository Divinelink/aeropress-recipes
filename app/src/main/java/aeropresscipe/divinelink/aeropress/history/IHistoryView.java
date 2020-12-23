package aeropresscipe.divinelink.aeropress.history;

import java.util.List;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;

interface IHistoryView {

    void showHistory(List<DiceDomain> savedRecipes);

    void showEmptyListMessage();
}
