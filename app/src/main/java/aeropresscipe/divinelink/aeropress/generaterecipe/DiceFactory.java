package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

abstract class Dice {

    protected ArrayList<Object> dice;

    abstract void setDice();

    public Object rollDice() {
        setDice();

        int randomIndex = (int) (Math.random() * this.dice.size());
        return this.dice.get(randomIndex);
    }
}


class Temperature extends Dice {

    @Override
    public void setDice() {
        dice.add(new TemperatureDice(80));
        dice.add(new TemperatureDice(85));
        dice.add(new TemperatureDice(90));
        dice.add(new TemperatureDice(95));
        dice.add(new TemperatureDice(100));
    }

    public Temperature() {
        this.dice = new ArrayList<>();
    }
}

class GroundSize extends Dice {

    @Override
    public void setDice() {
        dice.add(new GroundSizeDice("fine", 60));
        dice.add(new GroundSizeDice("medium-fine", 90));
        dice.add(new GroundSizeDice("medium", 120));
        dice.add(new GroundSizeDice("coarse", 240));
//        dice.add(new DiceDomain("lel", 5));
    }

    public GroundSize() {
        this.dice = new ArrayList<>();
    }

}


class BrewMethodDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new MethodDice("Standard", 0, 0));
        dice.add(new MethodDice("Standard", 30, 30));
        dice.add(new MethodDice("Standard", 30, 60));
        dice.add(new MethodDice("Inverted", 0, 0));
        dice.add(new MethodDice("Inverted", 30, 30));
        dice.add(new MethodDice("Inverted", 30, 60));
        dice.add(new MethodDice("Standard", 45, 30));
        dice.add(new MethodDice("Standard", 45, 60));
        dice.add(new MethodDice("Inverted", 45, 30));
        dice.add(new MethodDice("Inverted", 45, 60));

//        dice.add(new DiceDomain("TestNoBloom", 0, 0));

//        dice.add(new DiceDomain("Test", 1, 0));

    }

    public BrewMethodDice() {
        this.dice = new ArrayList<>();
    }

}

class BrewWaterAmountDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new BrewWaterDice(12, 200));
        dice.add(new BrewWaterDice(15, 200));
        dice.add(new BrewWaterDice(20, 200));
        dice.add(new BrewWaterDice(15, 250));
        dice.add(new BrewWaterDice(20, 250));
        dice.add(new BrewWaterDice(23, 250));
    }

    public BrewWaterAmountDice() {
        this.dice = new ArrayList<>();
    }
}

class GetRecipeFactory {
    public Object getRecipe(String diceType) {
        if (diceType == null) {
            return null;
        }
        if (diceType.equalsIgnoreCase("Temperature")) {
            return new Temperature().rollDice();
        } else if (diceType.equalsIgnoreCase("GroundSize")) {
            return new GroundSize().rollDice();
        } else if (diceType.equalsIgnoreCase("BrewMethod")) {
            return new BrewMethodDice().rollDice();
        } else if (diceType.equalsIgnoreCase("BrewWaterAmount")) {
            return new BrewWaterAmountDice().rollDice();
        }
        return null;
    }
}

class GenerateRecipe {

    GetRecipeFactory getRecipeFactory = new GetRecipeFactory();

    public Recipe getFinalRecipe() {

        TemperatureDice tempDice = (TemperatureDice) getRecipeFactory.getRecipe("Temperature");
        GroundSizeDice groundSizeDice = (GroundSizeDice) getRecipeFactory.getRecipe("GroundSize");
        MethodDice brewMethod = (MethodDice) getRecipeFactory.getRecipe("BrewMethod");
        BrewWaterDice brewWater = (BrewWaterDice) getRecipeFactory.getRecipe("BrewWaterAmount");


        final int temp = tempDice.getTemperature();

        final String groundSize = groundSizeDice.getGroundSize();
        final int brewTime = groundSizeDice.getBrewTime();

        final String brewingMethod = brewMethod.getBrewingMethod();
        final int bloomTime = brewMethod.getBloomTime();
        final int bloomWater = brewMethod.getBloomWater();

        final int waterAmount = brewWater.getBrewWater();
        final int coffeeAmount = brewWater.getCoffeeAmount();

        return new Recipe(
                temp,
                brewTime,
                bloomTime,
                bloomWater,
                coffeeAmount,
                waterAmount,
                groundSize,
                brewingMethod,
                false,
                false,
                0
        );
    }
}







