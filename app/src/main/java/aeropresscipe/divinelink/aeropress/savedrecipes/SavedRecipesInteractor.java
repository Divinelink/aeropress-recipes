package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

import java.util.List;

interface SavedRecipesInteractor {

    void getListsFromDB(SavedRecipesInteractor.OnGetSavedListsFromDBFinishListener listener, Context ctx);

    void deleteRecipeFromDB(SavedRecipesInteractor.OnGetRestFavouritesAfterDeletionFinishListener listener, SavedRecipeDomain recipe, Context ctx);

    void getSpecificRecipeFromDB(SavedRecipesInteractor.OnGetSingleRecipeFromDBFinishListener listener, Context ctx, SavedRecipeDomain recipe);

    interface OnGetSavedListsFromDBFinishListener{

        void onSuccess(List<SavedRecipeDomain> savedRecipes);
        void onEmptyList();
        void onError();

    }

    interface OnGetRestFavouritesAfterDeletionFinishListener{

        void onSuccessAfterDeletion(List<SavedRecipeDomain> savedRecipes);
        void onEmptyList();
        void onError();

    }

    interface OnGetSingleRecipeFromDBFinishListener{

        void onSuccessGetSingleRecipe(SavedRecipeDomain savedRecipeDomain);
        void onError();
    }


}
