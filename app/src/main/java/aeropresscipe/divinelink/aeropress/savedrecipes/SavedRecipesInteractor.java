package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

import java.util.List;

interface SavedRecipesInteractor {

    void getListsFromDB(SavedRecipesInteractor.OnGetSavedListsFromDBFinishListener listener, Context ctx);

    interface OnGetSavedListsFromDBFinishListener{

        void onSuccess(List<SavedRecipeDomain> savedRecipes);
        void onError();

    }


}
