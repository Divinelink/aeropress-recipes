package aeropresscipe.divinelink.aeropress.timer;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.generaterecipe.RecipeDao;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDao;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;
import androidx.annotation.RequiresApi;

public class TimerInteractorImpl implements TimerInteractor {

    @Override
    public void saveValues(OnStartTimerFinishListener listener, Context ctx, int bloomTime, int brewTime, boolean isBloomTimer) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();

        long endTimeBloom = System.currentTimeMillis() + bloomTime * 1000;
        long endTimeBrew = System.currentTimeMillis() + brewTime * 1000;

        editor.putLong("endTimeBloom", endTimeBloom);
        editor.putLong("endTimeBrew", endTimeBrew);
        editor.putBoolean("isBloomTimer", isBloomTimer);

        //Test for resuming brew
        editor.putBoolean("isBrewing", true);

        editor.apply();
    }

    @Override
    public void returnValues(OnStartTimerFinishListener listener, Context ctx) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);

        boolean isBloomTimer = preferences.getBoolean("isBloomTimer", true);
        long bloomTimeLeftInMillis = preferences.getLong("endTimeBloom", 0);
        long brewTimeLeftInMillis = preferences.getLong("endTimeBrew", 0);

        long minutes = (bloomTimeLeftInMillis - System.currentTimeMillis()) / 1000 / 60;
        long seconds = (bloomTimeLeftInMillis - System.currentTimeMillis()) / 1000 % 60;

        int endTime = (int) minutes * 60 + (int) seconds;


        if (endTime < 0) {
            if (isBloomTimer) {
                isBloomTimer = false;

                minutes = (brewTimeLeftInMillis - System.currentTimeMillis()) / 1000 / 60;
                seconds = (brewTimeLeftInMillis - System.currentTimeMillis()) / 1000 % 60;

                endTime = (int) minutes * 60 + (int) seconds;
                if (endTime < 0) // Brew Also Finished before resuming
                    //TODO add message to ask user either to go back or "Possible Feature": Add recipe to Liked Recipes.
                    listener.onBrewFinished();
                else
                    listener.onSuccess(endTime, isBloomTimer);
            } else
                listener.onBrewFinished();
        } else {
            listener.onSuccess(endTime, isBloomTimer);
        }

    }

    @Override
    public void saveLikedRecipe(final OnSaveLikedRecipeFinishListener listener, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();

                final DiceDomain recipe = recipeDao.getRecipe();
                final SavedRecipeDomain currentRecipe = new SavedRecipeDomain(recipe, getCurrentDate());

                final boolean recipeExists = savedRecipeDao.recipeExists(recipe.getId());

//                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();
//                SavedRecipeDomain currentRecipe = new SavedRecipeDomain(
//                        recipe.getId(), // FIXME pass object instead of properties
//                        recipe.getDiceTemperature(),
//                        recipe.getGroundSize(),
//                        recipe.getBrewTime(),
//                        recipe.getBrewingMethod(),
//                        recipe.getBloomTime(),
//                        recipe.getBloomWater(),
//                        recipe.getBrewWaterAmount(),
//                        recipe.getCoffeeAmount(),
//                        getCurrentDate()
//                        );


                listener.onRecipeFound(recipeExists);
//                if (!myData.contains(currentRecipe.getDiceDomain())) {
                if (!recipeExists) {
                    Log.d("Inserted", currentRecipe.toString());
                    savedRecipeDao.insertLikedRecipe(currentRecipe);
                    listener.onSuccessSave(true);
                } else {
                /*
                if (!checkIfCurrentRecipeExistsOnDB(myData, currentRecipe)) {
                    // If Recipe Doesn't Exist on DB, then Save It.
                    savedRecipeDao.insertLikedRecipe(currentRecipe);
                    Log.d("Inserted", currentRecipe.toString());
                    listener.onSuccessSave(true);
                } else {
                    // Otherwise, if it already exists (and user clicks on the button) then delete it.
                   /*
                    savedRecipeDao.deleteCurrent(recipe.getDiceTemperature(),
                            recipe.getGroundSize(),
                            recipe.getBrewTime(),
                            recipe.getBrewingMethod(),
                            recipe.getBloomTime(),
                            recipe.getBloomWater(),
                            recipe.getBrewWaterAmount(),
                            recipe.getCoffeeAmount());

                    */
                    Log.d("Deleted", currentRecipe.toString());
                    savedRecipeDao.delete(currentRecipe);
                    listener.onSuccessSave(false);
                }
            }
        });
    }

    @Override
    public void checkIfRecipeIsLikedAndSavedOnDB(final OnSaveLikedRecipeFinishListener listener, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                final SavedRecipeDao savedRecipeDao = HomeDatabase.getDatabase(ctx).savedRecipeDao();

                final DiceDomain recipe = recipeDao.getRecipe();
                final SavedRecipeDomain currentRecipe = new SavedRecipeDomain(recipe, getCurrentDate());
                final boolean recipeExists = savedRecipeDao.recipeExists(recipe.getId()); //Checks whether current recipe exists on DB
/*
                final List<SavedRecipeDomain> myData = savedRecipeDao.getSavedRecipes();
                final boolean exists = savedRecipeDao.recipeExists(recipe);
                SavedRecipeDomain currentRecipe = new SavedRecipeDomain(
                        recipe.getId(), //FIXME  pass object instead of properties
                        recipe.getDiceTemperature(),
                        recipe.getGroundSize(),
                        recipe.getBrewTime(),
                        recipe.getBrewingMethod(),
                        recipe.getBloomTime(),
                        recipe.getBloomWater(),
                        recipe.getBrewWaterAmount(),
                        recipe.getCoffeeAmount(),
                        getCurrentDate()
                );
*/
//                if (myData.contains(currentRecipe))
//                    listener.onRecipeFound(true);
//                else
//                    listener.onRecipeFound(false);

                // returns true if currentRecipe exists on myData DB
//                boolean contains = myData.contains(currentRecipe);
//                listener.onRecipeFound(checkIfCurrentRecipeExistsOnDB(myData, currentRecipe));
/*
                if (checkIfCurrentRecipeExistsOnDB(myData, currentRecipe)) {
                    listener.onRecipeFound(true);
                    Log.d("Recipe Exists in DB?", "Yes");
                } else {
                    listener.onRecipeFound(false);
                    Log.d("Recipe Exists in DB?", "Unlucky");
                }
*/
                Log.d("Current Recipe: ", currentRecipe.toString());
                listener.onRecipeFound(recipeExists);
            }
        });
    }
/*
    public boolean checkIfCurrentRecipeExistsOnDB(List<SavedRecipeDomain> myData, SavedRecipeDomain currentRecipe) {
        boolean recipeFound = false;

        for (SavedRecipeDomain dataInDB : myData) {

            recipeFound = dataInDB.getDiceTemperature() == currentRecipe.getDiceTemperature() &&
                    dataInDB.getGroundSize().equals(currentRecipe.getGroundSize()) &&
                    dataInDB.getBrewTime() == currentRecipe.getBrewTime() &&
                    dataInDB.getBrewingMethod().equals(currentRecipe.getBrewingMethod()) &&
                    dataInDB.getBloomTime() == currentRecipe.getBloomTime() &&
                    dataInDB.getBloomWater() == currentRecipe.getBloomWater() &&
                    dataInDB.getBrewWaterAmount() == currentRecipe.getBrewWaterAmount() &&
                    dataInDB.getCoffeeAmount() == currentRecipe.getCoffeeAmount();

            if (recipeFound)
                break;
        }
        */

//            if (dataInDB == currentRecipe) {
//                recipeFound = true;
//                break;
//            }
//            */
//            if (recipeFound)
//                break;
//        }
//        return recipeFound;
//    }

    public String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy");

        return formatter.format(date.getTime());
    }

}
