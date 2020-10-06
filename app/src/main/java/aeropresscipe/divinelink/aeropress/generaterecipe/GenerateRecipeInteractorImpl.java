package aeropresscipe.divinelink.aeropress.generaterecipe;


import java.util.ArrayList;

public class GenerateRecipeInteractorImpl implements GenerateRecipeInteractor{
            //We only want to edit out data in the interactor, before we pass them into the Presenter
            // otherwise we break the MVP rules
            @Override
            public void getRecipe(OnGenerateRecipeFinishListener listener) {

                ArrayList<DiceDomain> tempDice = addTemperatureDiceProperties();
                ArrayList<DiceDomain> groundSizeDice = addGroundSizeDiceProperties();
                ArrayList<DiceDomain> brewingMethodDice = addBrewingMethodProperties();
                ArrayList<DiceDomain> waterAmountDice = addBrewingWaterAmountProperties();

                // Generate random profile
                int randomTempIndex = (int) (Math.random() * tempDice.size());
                int randomGroundSizeIndex = (int) (Math.random() * groundSizeDice.size());
                int randomBrewingMethodIndex = (int) (Math.random() * brewingMethodDice.size());
                int randomWaterAmountIndex = (int) (Math.random() * waterAmountDice.size());

                int temp = tempDice.get(randomTempIndex).getDiceTemperature();
                String groundSize = groundSizeDice.get(randomGroundSizeIndex).getGroundSize();
                int brewTime = groundSizeDice.get(randomGroundSizeIndex).getBrewTime();

                String brewingMethod = brewingMethodDice.get(randomBrewingMethodIndex).getBrewingMethod();
                int bloomTime = brewingMethodDice.get(randomBrewingMethodIndex).getBloomTime();
                int bloomWater = brewingMethodDice.get(randomBrewingMethodIndex).getBloomWater();

                int waterAmount = waterAmountDice.get(randomWaterAmountIndex).getBrewWaterAmount();
                int coffeeAmount = waterAmountDice.get(randomBrewingMethodIndex).getCoffeeAmount();

            //    listener.onSuccess(temperature, groundSize, brewingMethod, waterAmount);
                listener.onSuccess(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);
            }

            private ArrayList<DiceDomain> addTemperatureDiceProperties() {

                ArrayList<DiceDomain> TemperatureDice = new ArrayList<>();

                DiceDomain tempDice1 = new DiceDomain(1, 80);
                DiceDomain tempDice2 = new DiceDomain(1, 85);
                DiceDomain tempDice3 = new DiceDomain(1, 90);
                DiceDomain tempDice4 = new DiceDomain(1, 95);
                DiceDomain tempDice5 = new DiceDomain(1, 100);

                TemperatureDice.add(tempDice1);
                TemperatureDice.add(tempDice2);
                TemperatureDice.add(tempDice3);
                TemperatureDice.add(tempDice4);
                TemperatureDice.add(tempDice5);

                return TemperatureDice;
            }

            private ArrayList<DiceDomain> addGroundSizeDiceProperties(){

                ArrayList<DiceDomain> GroundSizeDice = new ArrayList<>();
                DiceDomain groundSizeDice1 = new DiceDomain(2,"fine", 60);
                DiceDomain groundSizeDice2 = new DiceDomain(2,"medium-fine", 90);
                DiceDomain groundSizeDice3 = new DiceDomain(2,"medium", 120);
                DiceDomain groundSizeDice4 = new DiceDomain(2,"coarse", 240);

                GroundSizeDice.add(groundSizeDice1);
                GroundSizeDice.add(groundSizeDice2);
                GroundSizeDice.add(groundSizeDice3);
                GroundSizeDice.add(groundSizeDice4);

                return GroundSizeDice;
            }

            private ArrayList<DiceDomain> addBrewingMethodProperties(){

                ArrayList<DiceDomain> BrewingMethodDice = new ArrayList<>();

                DiceDomain brewingMethodDice1 = new DiceDomain(3, "Standard", 0, 0);
                DiceDomain brewingMethodDice2 = new DiceDomain(3, "Standard", 30, 30);
                DiceDomain brewingMethodDice3 = new DiceDomain(3, "Standard", 30, 60);
                DiceDomain brewingMethodDice4 = new DiceDomain(3, "Inverted", 0, 0);
                DiceDomain brewingMethodDice5 = new DiceDomain(3, "Inverted", 30, 30);
                DiceDomain brewingMethodDice6 = new DiceDomain(3, "Inverted", 30, 60);

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

                DiceDomain brewingAmountDice1 = new DiceDomain(4, 12, 200);
                DiceDomain brewingAmountDice2 = new DiceDomain(4, 15, 200);
                DiceDomain brewingAmountDice3 = new DiceDomain(4, 15, 250);
                DiceDomain brewingAmountDice4 = new DiceDomain(4, 20, 200);
                DiceDomain brewingAmountDice5 = new DiceDomain(4, 20, 250);
                DiceDomain brewingAmountDice6 = new DiceDomain(4, 23, 250);

                BrewingWaterAmountDice.add(brewingAmountDice1);
                BrewingWaterAmountDice.add(brewingAmountDice2);
                BrewingWaterAmountDice.add(brewingAmountDice3);
                BrewingWaterAmountDice.add(brewingAmountDice4);
                BrewingWaterAmountDice.add(brewingAmountDice5);
                BrewingWaterAmountDice.add(brewingAmountDice6);

                return BrewingWaterAmountDice;
            }
    
}
