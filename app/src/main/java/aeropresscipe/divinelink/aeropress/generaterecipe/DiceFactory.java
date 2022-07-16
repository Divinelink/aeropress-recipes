package aeropresscipe.divinelink.aeropress.generaterecipe;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe;

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
//        dice.add(new GroundSizeDice(CoffeeGroundSize.FINE, 60));
//        dice.add(new GroundSizeDice(CoffeeGroundSize.MEDIUM_FINE, 90));
//        dice.add(new GroundSizeDice(CoffeeGroundSize.MEDIUM, 120));
//        dice.add(new GroundSizeDice(CoffeeGroundSize.COARSE, 240));
        dice.add(new GroundSizeDice(CoffeeGrindSize.FINE, 10));
        dice.add(new GroundSizeDice(CoffeeGrindSize.MEDIUM_FINE, 10));
        dice.add(new GroundSizeDice(CoffeeGrindSize.MEDIUM, 10));
        dice.add(new GroundSizeDice(CoffeeGrindSize.COARSE, 10));
        dice.add(new GroundSizeDice(CoffeeGrindSize.COARSE, 60));
        dice.add(new GroundSizeDice(CoffeeGrindSize.COARSE, 65));

    }

    public GroundSize() {
        this.dice = new ArrayList<>();
    }

}


class BrewMethodDice extends Dice {

    @Override
    public void setDice() {
//        dice.add(new MethodDice("Standard", 0, 0));
//        dice.add(new MethodDice("Standard", 30, 30));
//        dice.add(new MethodDice("Standard", 30, 60));
//        dice.add(new MethodDice("Inverted", 0, 0));
//        dice.add(new MethodDice("Inverted", 30, 30));
//        dice.add(new MethodDice("Inverted", 30, 60));
//        dice.add(new MethodDice("Standard", 45, 30));
//        dice.add(new MethodDice("Standard", 45, 60));
//        dice.add(new MethodDice("Inverted", 45, 30));
//        dice.add(new MethodDice("Inverted", 45, 60));

//        dice.add(new DiceDomain("TestNoBloom", 0, 0));

        dice.add(new MethodDice(BrewMethod.INVERTED, 5, 60));
        dice.add(new MethodDice(BrewMethod.STANDARD, 5, 60));

        dice.add(new MethodDice(BrewMethod.STANDARD, 0, 0));

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

        final CoffeeGrindSize groundSize = groundSizeDice.getGroundSize();
        final int brewTime = groundSizeDice.getBrewTime();

        final BrewMethod brewingMethod = brewMethod.getBrewMethod();
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
                true
        );
    }
}







