package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

import java.util.List;

interface SavedRecipesInteractor {

    void getListsFromDB(SavedRecipesInteractor.OnGetSavedListsFromDBFinishListener listener, Context ctx);

    void deleteRecipeFromDB(SavedRecipesInteractor.OnGetSavedListsFromDBFinishListener listener, SavedRecipeDomain recipeDomain, Context ctx);

    void getSpecificRecipeFromDB(SavedRecipesInteractor.OnGetSingleRecipeFromDBFinishListener listener, Context ctx, int position);

    interface OnGetSavedListsFromDBFinishListener{

        void onSuccess(List<SavedRecipeDomain> savedRecipes);
        void onError();

    }

    interface OnGetSingleRecipeFromDBFinishListener{

        void onSuccessGetSingleRecipe(SavedRecipeDomain savedRecipeDomain);
        void onError();
    }


}
