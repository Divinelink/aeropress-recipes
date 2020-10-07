package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import android.os.AsyncTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import aeropresscipe.divinelink.aeropress.base.HomeDatabase;

public class GenerateRecipeInteractorImpl implements GenerateRecipeInteractor {
    //We only want to edit out data in the interactor, before we pass them into the Presenter
    // otherwise we break the MVP rules

    @Override
    public void getRecipe(final OnGenerateRecipeFinishListener listener, final Context ctx) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final RecipeDao recipeDao = HomeDatabase.getDatabase(ctx).recipeDao();
                final DiceDomain recipe = recipeDao.getRecipe();

                if (recipe == null) {
                    ArrayList<DiceDomain> tempDice = addTemperatureDiceProperties();
                    ArrayList<DiceDomain> groundSizeDice = addGroundSizeDiceProperties();
                    ArrayList<DiceDomain> brewingMethodDice = addBrewingMethodProperties();
                    ArrayList<DiceDomain> waterAmountDice = addBrewingWaterAmountProperties();
                    //TODO Instead of getting a random item on getRecipe Method,
                    // make it return a single random element from each method below.
                    //Generate random profile
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

                    final DiceDomain newRecipe = new DiceDomain(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            // When DB is empty, meaning it has no recipe, it automatically saved the current recipe on the DB
                            // And we show it on the fragment.
                            recipeDao.updateRecipe(newRecipe);
                            listener.onSuccess(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);
                        }
                    });
                } else {
                    listener.onSuccess(recipe.getDiceTemperature(), recipe.getGroundSize(), recipe.getBrewTime(), recipe.getBrewingMethod(), recipe.getBloomTime(), recipe.getBloomWater(), recipe.getBrewWaterAmount(), recipe.getCoffeeAmount());
                    //recipeDao.deleteAll();
                }
            }
        });
    }


    @Override
    public void getSavedRecipe(OnGenerateRecipeFinishListener listener) {

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

        GroundSizeDice.add(groundSizeDice1);
        GroundSizeDice.add(groundSizeDice2);
        GroundSizeDice.add(groundSizeDice3);
        GroundSizeDice.add(groundSizeDice4);

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

        BrewingMethodDice.add(brewingMethodDice1);
        BrewingMethodDice.add(brewingMethodDice2);
        BrewingMethodDice.add(brewingMethodDice3);
        BrewingMethodDice.add(brewingMethodDice4);
        BrewingMethodDice.add(brewingMethodDice5);
        BrewingMethodDice.add(brewingMethodDice6);

        return BrewingMethodDice;
    }

    private ArrayList<DiceDomain> addBrewingWaterAmountProperties() {
        ArrayList<DiceDomain> BrewingWaterAmountDice = new ArrayList<>();

        DiceDomain brewingAmountDice1 = new DiceDomain(12, 200);
        DiceDomain brewingAmountDice2 = new DiceDomain(15, 200);
        DiceDomain brewingAmountDice3 = new DiceDomain(15, 250);
        DiceDomain brewingAmountDice4 = new DiceDomain(20, 200);
        DiceDomain brewingAmountDice5 = new DiceDomain(20, 250);
        DiceDomain brewingAmountDice6 = new DiceDomain(23, 250);

        BrewingWaterAmountDice.add(brewingAmountDice1);
        BrewingWaterAmountDice.add(brewingAmountDice2);
        BrewingWaterAmountDice.add(brewingAmountDice3);
        BrewingWaterAmountDice.add(brewingAmountDice4);
        BrewingWaterAmountDice.add(brewingAmountDice5);
        BrewingWaterAmountDice.add(brewingAmountDice6);

        return BrewingWaterAmountDice;
    }

}
