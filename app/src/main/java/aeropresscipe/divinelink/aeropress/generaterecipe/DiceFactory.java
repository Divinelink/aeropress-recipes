package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

abstract class Dice {

    protected ArrayList<DiceDomain> dice;

    abstract void setDice();

    public DiceDomain rollDice() {
        setDice();

        int randomIndex = (int) (Math.random() * this.dice.size());
        return this.dice.get(randomIndex);
    }
}


class TemperatureDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new DiceDomain(80));
        dice.add(new DiceDomain(85));
        dice.add(new DiceDomain(90));
        dice.add(new DiceDomain(95));
        dice.add(new DiceDomain(100));
    }

    public TemperatureDice() {
        this.dice = new ArrayList<>();
    }
}

class GroundSizeDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new DiceDomain("fine", 60));
        dice.add(new DiceDomain("medium-fine", 90));
        dice.add(new DiceDomain("medium", 120));
        dice.add(new DiceDomain("coarse", 240));
    }

    public GroundSizeDice() {
        this.dice = new ArrayList<>();
    }

}

class BrewMethodDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new DiceDomain("Standard", 0, 0));
        dice.add(new DiceDomain("Standard", 30, 30));
        dice.add(new DiceDomain("Standard", 30, 60));
        dice.add(new DiceDomain("Inverted", 0, 0));
        dice.add(new DiceDomain("Inverted", 30, 30));
        dice.add(new DiceDomain("Inverted", 30, 60));
        dice.add(new DiceDomain("Standard", 45, 30));
        dice.add(new DiceDomain("Standard", 45, 60));
        dice.add(new DiceDomain("Inverted", 45, 30));
        dice.add(new DiceDomain("Inverted", 45, 60));

    }

    public BrewMethodDice() {
        this.dice = new ArrayList<>();
    }

}

class BrewWaterAmountDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new DiceDomain(12, 200));
        dice.add(new DiceDomain(15, 200));
        dice.add(new DiceDomain(15, 250));
        dice.add(new DiceDomain(20, 200));
        dice.add(new DiceDomain(20, 250));
        dice.add(new DiceDomain(23, 300));
    }

    public BrewWaterAmountDice() {
        this.dice = new ArrayList<>();
    }
}

class GetRecipeFactory{
    public DiceDomain getRecipe(String diceType){
        if(diceType == null){
            return null;
        }
        if(diceType.equalsIgnoreCase("Temperature")) {
            return new TemperatureDice().rollDice();
        }
        else if(diceType.equalsIgnoreCase("GroundSize")){
            return new GroundSizeDice().rollDice();
        }
        else if(diceType.equalsIgnoreCase("BrewMethod")) {
            return new BrewMethodDice().rollDice();
        }
        else if(diceType.equalsIgnoreCase("BrewWaterAmount")){
            return new BrewWaterAmountDice().rollDice();
        }
        return null;
    }
}

class GenerateRecipe {

    GetRecipeFactory getRecipeFactory = new GetRecipeFactory();

    public DiceDomain getFinalRecipe() {

        DiceDomain tempDice = getRecipeFactory.getRecipe("Temperature");
        DiceDomain groundSizeDice = getRecipeFactory.getRecipe("GroundSize");
        DiceDomain brewMethod = getRecipeFactory.getRecipe("BrewMethod");
        DiceDomain brewWater = getRecipeFactory.getRecipe("BrewWaterAmount");


        final int temp = tempDice.getDiceTemperature();

        final String groundSize = groundSizeDice.getGroundSize();
        final int brewTime = groundSizeDice.getBrewTime();

        final String brewingMethod = brewMethod.getBrewingMethod();
        final int bloomTime = brewMethod.getBloomTime();
        final int bloomWater = brewMethod.getBloomWater();

        final int waterAmount = brewWater.getBrewWaterAmount();
        final int coffeeAmount = brewWater.getCoffeeAmount();

        return new DiceDomain(temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount);
    }
}







