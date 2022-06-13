package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

import java.util.List;

public class SavedRecipesPresenterImpl implements SavedRecipesPresenter, SavedRecipesInteractor.OnGetSavedListsFromDBFinishListener, SavedRecipesInteractor.OnGetSingleRecipeFromDBFinishListener, SavedRecipesInteractor.OnGetRestFavouritesAfterDeletionFinishListener {


    final private SavedRecipesView savedRecipesView;
    final private SavedRecipesInteractor interactor;


    public SavedRecipesPresenterImpl(SavedRecipesView recipesView) {
        this.savedRecipesView = recipesView;
        interactor = new SavedRecipesInteractorImpl();
    }

    @Override
    public void getSavedRecipes(Context ctx) {
        interactor.getListsFromDB(this, ctx);
    }

    @Override
    public void onSuccess(List<SavedRecipeDomain> savedRecipes) {

        savedRecipesView.showSavedRecipes(savedRecipes);

    }



    @Override
    public void deleteRecipe(SavedRecipeDomain recipeDomain, Context ctx) {
        interactor.deleteRecipeFromDB(this, recipeDomain, ctx);
    }

    @Override
    public void onError() {
      // Intentionally Empty
    }

    @Override
    public void getSpecificRecipeToStartNewBrew(Context ctx, int position) {
        interactor.getSpecificRecipeFromDB(this, ctx, position);

    }

    @Override
    public void onSuccessGetSingleRecipe(SavedRecipeDomain savedRecipeDomain) {

        savedRecipesView.passData(savedRecipeDomain.getBloomTime(),
                savedRecipeDomain.getBrewTime(),
                savedRecipeDomain.getBloomWater(),
                savedRecipeDomain.getBrewWaterAmount() - savedRecipeDomain.getBloomWater());

    }

    @Override
    public void onSuccessAfterDeletion(List<SavedRecipeDomain> savedRecipes) {
        savedRecipesView.showSavedRecipesAfterDeletion(savedRecipes);
    }

    @Override
    public void onEmptyList() {
        savedRecipesView.showEmptyListMessage();
    }
}
