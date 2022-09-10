package aeropresscipe.divinelink.aeropress.generaterecipe.factory;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.generaterecipe.models.BrewMethod;
import aeropresscipe.divinelink.aeropress.generaterecipe.models.CoffeeGrindSize;
import aeropresscipe.divinelink.aeropress.generaterecipe.models.RecipeDice;

abstract class Dice {

    protected ArrayList<RecipeDice> dice;

    abstract void setDice();

    public RecipeDice rollDice() {
        setDice();

        int randomIndex = (int) (Math.random() * this.dice.size());
        return this.dice.get(randomIndex);
    }
}


class Temperature extends Dice {

    @Override
    public void setDice() {
        dice.add(new RecipeDice.TemperatureDice(80));
        dice.add(new RecipeDice.TemperatureDice(85));
        dice.add(new RecipeDice.TemperatureDice(90));
        dice.add(new RecipeDice.TemperatureDice(95));
        dice.add(new RecipeDice.TemperatureDice(100));
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
        dice.add(new RecipeDice.GroundSizeDice(CoffeeGrindSize.FINE, 10));
        dice.add(new RecipeDice.GroundSizeDice(CoffeeGrindSize.MEDIUM_FINE, 10));
        dice.add(new RecipeDice.GroundSizeDice(CoffeeGrindSize.MEDIUM, 10));
        dice.add(new RecipeDice.GroundSizeDice(CoffeeGrindSize.COARSE, 10));
        dice.add(new RecipeDice.GroundSizeDice(CoffeeGrindSize.COARSE, 60));
        dice.add(new RecipeDice.GroundSizeDice(CoffeeGrindSize.COARSE, 65));

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

        dice.add(new RecipeDice.MethodDice(BrewMethod.INVERTED, 5, 60));
        dice.add(new RecipeDice.MethodDice(BrewMethod.STANDARD, 5, 60));

        dice.add(new RecipeDice.MethodDice(BrewMethod.STANDARD, 0, 0));

//        dice.add(new DiceDomain("Test", 1, 0));

    }

    public BrewMethodDice() {
        this.dice = new ArrayList<>();
    }

}

class BrewWaterAmountDice extends Dice {

    @Override
    public void setDice() {
        dice.add(new RecipeDice.BrewWaterDice(12, 200));
        dice.add(new RecipeDice.BrewWaterDice(15, 200));
        dice.add(new RecipeDice.BrewWaterDice(20, 200));
        dice.add(new RecipeDice.BrewWaterDice(15, 250));
        dice.add(new RecipeDice.BrewWaterDice(20, 250));
        dice.add(new RecipeDice.BrewWaterDice(23, 250));
    }

    public BrewWaterAmountDice() {
        this.dice = new ArrayList<>();
    }
}
