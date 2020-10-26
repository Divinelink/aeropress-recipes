package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;

import java.util.List;

public class SavedRecipesPresenterImpl implements SavedRecipesPresenter, SavedRecipesInteractor.OnGetSavedListsFromDBFinishListener {


    private SavedRecipesView generateRecipeView;
    private SavedRecipesInteractor interactor;


    public SavedRecipesPresenterImpl(SavedRecipesView recipesView) {
        this.generateRecipeView = recipesView;
        interactor = new SavedRecipesInteractorImpl();
    }

    @Override
    public void getSavedRecipes(Context ctx) {
        interactor.getListsFromDB(this, ctx);
    }

    @Override
    public void onSuccess(List<SavedRecipeDomain> savedRecipes) {

        generateRecipeView.showSavedRecipes(savedRecipes);

    }

    @Override
    public void onError() {



    }
}
