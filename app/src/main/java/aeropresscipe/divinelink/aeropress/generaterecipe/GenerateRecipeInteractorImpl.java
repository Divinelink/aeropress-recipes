package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;

public class GenerateRecipeInteractorImpl implements GenerateRecipeInteractor, SharedPrefManager {
    //We only want to edit out data in the interactor, before we pass them into the Presenter
    // otherwise we break the MVP rules


    @Override
    public void getNewRecipe(final OnGenerateRecipeFinishListener listener, final Context ctx, boolean letGenerate) {
        // Executed By Pressing "Get Recipe Button"

        // Check whether to show generate a new recipe or not.
        // If there's a recipe running already, show a toast that asks the user to long press on the button
        // in order to get a new recipe.
        // When long-pressing, letGenerate is true, and isBrewing becomes false, then the model passes the new recipe on the presenter etc.
        boolean isBrewing = IsItBrewingAlready(ctx);

        if (letGenerate)
            isBrewing = false;

        if (isBrewing) {
            listener.isAlreadyBrewing();
        } else {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                    final DiceDomain newRecipe = getRandomRecipe();

                    // When Using AsyncTask, we also need to use .runOnUiThread(new Runnable()) where we set the adapter
                    // That updates the UI. In this case, on the fragment, on showRecipe() method.

                    // Stuff that updates the UI
                    Log.d("getNewRecipe", "Get New Recipe Button, updates DB and creates new recipe");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            recipeDao.updateRecipe(newRecipe);
                            listener.onSuccessNewRecipe(
                                    newRecipe.getDiceTemperature(),
                                    newRecipe.getGroundSize(),
                                    newRecipe.getBrewTime(),
                                    newRecipe.getBrewingMethod(),
                                    newRecipe.getBloomTime(),
                                    newRecipe.getBloomWater(),
                                    newRecipe.getBrewWaterAmount(),
                                    newRecipe.getCoffeeAmount()
                            );
                        }
                    });

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isBrewing", false);

                    editor.apply();
                }
            });
        }
    }

    @Override
    public void getRecipe(final OnGenerateRecipeFinishListener listener, final Context ctx) {

        final boolean isBrewing = IsItBrewingAlready(ctx);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                final DiceDomain recipe = recipeDao.getRecipe();
                // First time app starts, recipe == null, so we create a new one.
                if (recipe == null) {
                    // If it's the first time we run the app, there's no recipe. We generate a new one using the getRandomRecipe() method
                    final DiceDomain newRecipe = getRandomRecipe();
                        // When DB is empty, meaning it has no recipe, it automatically saves the current recipe on the DB
                        // And we show it on the fragment.
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("getRecipe", "Creates New Recipe when app starts and there's no recipe available.");
                                recipeDao.updateRecipe(newRecipe);
                                listener.onSuccessAppStarts(
                                        newRecipe.getDiceTemperature(),
                                        newRecipe.getGroundSize(),
                                        newRecipe.getBrewTime(),
                                        newRecipe.getBrewingMethod(),
                                        newRecipe.getBloomTime(),
                                        newRecipe.getBloomWater(),
                                        newRecipe.getBrewWaterAmount(),
                                        newRecipe.getCoffeeAmount()
                                );
                            }
                        });
                } else {
                    Log.d("Show Recipe", "Show already existing recipe");
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            if (isBrewing) // When there's a timer active and app is starting
                                listener.onSuccess(
                                        recipe.getDiceTemperature(),
                                        recipe.getGroundSize(),
                                        recipe.getBrewTime(),
                                        recipe.getBrewingMethod(),
                                        recipe.getBloomTime(),
                                        recipe.getBloomWater(),
                                        recipe.getBrewWaterAmount(),
                                        recipe.getCoffeeAmount());
                            else // When there's no timer active and app is starting
                                listener.onSuccessAppStarts(
                                        recipe.getDiceTemperature(),
                                        recipe.getGroundSize(),
                                        recipe.getBrewTime(),
                                        recipe.getBrewingMethod(),
                                        recipe.getBloomTime(),
                                        recipe.getBloomWater(),
                                        recipe.getBrewWaterAmount(),
                                        recipe.getCoffeeAmount());
                        }
                    });
                }
            }
        });
    }

    private DiceDomain getRandomRecipe() {
        ArrayList<DiceDomain> tempDice = addTemperatureDiceProperties();
        ArrayList<DiceDomain> groundSizeDice = addGroundSizeDiceProperties();
        ArrayList<DiceDomain> brewingMethodDice = addBrewingMethodProperties();
        ArrayList<DiceDomain> waterAmountDice = addBrewingWaterAmountProperties();

        int randomTempIndex = (int) (Math.random() * tempDice.size());
        int randomGroundSizeIndex = (int) (Math.random() * groundSizeDice.size());
        int randomBrewingMethodIndex = (int) (Math.random() * brewingMethodDice.size());
        int randomWaterAmountIndex = (int) (Math.random() * waterAmountDice.size());

        final int temp = tempDice.get(randomTempIndex).getDiceTemperature();
        final String groundSize = groundSizeDice.get(randomGroundSizeIndex).getGroundSize();
        final int brewTime = groundSizeDice.get(randomGroundSizeIndex).getBrewTime();

        final String brewingMethod = brewingMethodDice.get(randomBrewingMethodIndex).getBrewingMethod();
        final int bloomTime = brewingMethodDice.get(randomBrewingMethodIndex).getBloomTime();
        final int bloomWater = brewingMethodDice.get(randomBrewingMethodIndex).getBloomWater();

        final int waterAmount = waterAmountDice.get(randomWaterAmountIndex).getBrewWaterAmount();
        final int coffeeAmount = waterAmountDice.get(randomWaterAmountIndex).getCoffeeAmount();

        //Return DiceDomain Object which is a random generated recipe.
        return new DiceDomain(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);
    }

    private ArrayList<DiceDomain> addTemperatureDiceProperties() {

        ArrayList<DiceDomain> TemperatureDice = new ArrayList<>();

        DiceDomain tempDice1 = new DiceDomain(80);
        DiceDomain tempDice2 = new DiceDomain(85);
        DiceDomain tempDice3 = new DiceDomain(90);
        DiceDomain tempDice4 = new DiceDomain(95);
        DiceDomain tempDice5 = new DiceDomain(100);

        TemperatureDice.add(tempDice1);
        TemperatureDice.add(tempDice2);
        TemperatureDice.add(tempDice3);
        TemperatureDice.add(tempDice4);
        TemperatureDice.add(tempDice5);

        return TemperatureDice;
    }

    private ArrayList<DiceDomain> addGroundSizeDiceProperties() {

        ArrayList<DiceDomain> GroundSizeDice = new ArrayList<>();

        DiceDomain groundSizeDice1 = new DiceDomain("fine", 60);
        DiceDomain groundSizeDice2 = new DiceDomain("medium-fine", 90);
        DiceDomain groundSizeDice3 = new DiceDomain("medium", 120);
        DiceDomain groundSizeDice4 = new DiceDomain("coarse", 240);
        DiceDomain groundSizeDice5 = new DiceDomain("test", 10);

        GroundSizeDice.add(groundSizeDice1);
        GroundSizeDice.add(groundSizeDice2);
        GroundSizeDice.add(groundSizeDice3);
        GroundSizeDice.add(groundSizeDice4);
        //TEST
        //  GroundSizeDice.add(groundSizeDice5);

        return GroundSizeDice;
    }

    private ArrayList<DiceDomain> addBrewingMethodProperties() {

        ArrayList<DiceDomain> BrewingMethodDice = new ArrayList<>();

        DiceDomain brewingMethodDice1 = new DiceDomain("Standard", 0, 0);
        DiceDomain brewingMethodDice2 = new DiceDomain("Standard", 30, 30);
        DiceDomain brewingMethodDice3 = new DiceDomain("Standard", 30, 60);
        DiceDomain brewingMethodDice4 = new DiceDomain("Inverted", 0, 0);
        DiceDomain brewingMethodDice5 = new DiceDomain("Inverted", 30, 30);
        DiceDomain brewingMethodDice6 = new DiceDomain("Inverted", 30, 60);
        DiceDomain brewingMethodDice7 = new DiceDomain("Standard", 45, 30);
        DiceDomain brewingMethodDice8 = new DiceDomain("Standard", 45, 60);
        DiceDomain brewingMethodDice9 = new DiceDomain("Inverted", 45, 30);
        DiceDomain brewingMethodDice10 = new DiceDomain("Inverted", 45, 60);


        BrewingMethodDice.add(brewingMethodDice1);
        BrewingMethodDice.add(brewingMethodDice2);
        BrewingMethodDice.add(brewingMethodDice3);
        BrewingMethodDice.add(brewingMethodDice4);
        BrewingMethodDice.add(brewingMethodDice5);
        BrewingMethodDice.add(brewingMethodDice6);
        BrewingMethodDice.add(brewingMethodDice7);
        BrewingMethodDice.add(brewingMethodDice8);
        BrewingMethodDice.add(brewingMethodDice9);
        BrewingMethodDice.add(brewingMethodDice10);


        return BrewingMethodDice;
    }

    private ArrayList<DiceDomain> addBrewingWaterAmountProperties() {
        ArrayList<DiceDomain> BrewingWaterAmountDice = new ArrayList<>();

        DiceDomain brewingAmountDice1 = new DiceDomain(12, 200);
        DiceDomain brewingAmountDice2 = new DiceDomain(15, 200);
        DiceDomain brewingAmountDice3 = new DiceDomain(15, 250);
        DiceDomain brewingAmountDice4 = new DiceDomain(20, 200);
        DiceDomain brewingAmountDice5 = new DiceDomain(20, 250);
        DiceDomain brewingAmountDice6 = new DiceDomain(23, 300);

        BrewingWaterAmountDice.add(brewingAmountDice1);
        BrewingWaterAmountDice.add(brewingAmountDice2);
        BrewingWaterAmountDice.add(brewingAmountDice3);
        BrewingWaterAmountDice.add(brewingAmountDice4);
        BrewingWaterAmountDice.add(brewingAmountDice5);
        BrewingWaterAmountDice.add(brewingAmountDice6);

        return BrewingWaterAmountDice;
    }

    @Override
    public boolean IsItBrewingAlready(Context ctx) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getBoolean("isBrewing", false);

    }
}
