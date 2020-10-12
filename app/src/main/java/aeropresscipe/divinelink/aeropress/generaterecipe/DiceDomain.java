package aeropresscipe.divinelink.aeropress.generaterecipe;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Recipe")
public class DiceDomain {

    @PrimaryKey
    @NonNull
    private int diceTemperature;
    private String GroundSize;
    private int brewTime;
    private String brewingMethod;
    private int bloomTime;
    private int bloomWater;

    private int coffeeAmount;
    private int brewWaterAmount;

    public DiceDomain(){};

    public DiceDomain(int diceTemperature, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int brewWaterAmount, int coffeeAmount) {
        this.diceTemperature = diceTemperature;
        this.GroundSize = groundSize;
        this.brewTime = brewTime;
        this.brewingMethod = brewingMethod;
        this.bloomTime = bloomTime;
        this.bloomWater = bloomWater;
        this.brewWaterAmount = brewWaterAmount;
        this.coffeeAmount = coffeeAmount;
    }
    @Ignore
    public DiceDomain( String groundSize, int brewTime) {
        this.GroundSize = groundSize;
        this.brewTime = brewTime;
    }
    @Ignore
        public DiceDomain(int coffeeAmount, int brewWaterAmount) {
        this.coffeeAmount = coffeeAmount;
        this.brewWaterAmount = brewWaterAmount;
    }
    @Ignore
    public DiceDomain( int diceTemperature) {

        this.diceTemperature = diceTemperature;
    }
    @Ignore
    public DiceDomain( String brewingMethod, int bloomTime, int bloomWater) {
        this.brewingMethod = brewingMethod;
        this.bloomTime = bloomTime;
        this.bloomWater = bloomWater;
    }

    public int getBrewWaterAmount() {
        return brewWaterAmount;
    }

    public int getDiceTemperature() {
        return diceTemperature;
    }

    public String getGroundSize() {
        return GroundSize;
    }

    public int getBrewTime() {
        return brewTime;
    }

    public String getBrewingMethod() {
        return brewingMethod;
    }

    public int getBloomTime() {
        return bloomTime;
    }

    public int getBloomWater() {
        return bloomWater;
    }

    public int getCoffeeAmount() {
        return coffeeAmount;
    }

    public void setDiceTemperature(int diceTemperature) {
        this.diceTemperature = diceTemperature;
    }



    public void setGroundSize(String groundSize) {
        GroundSize = groundSize;
    }

    public void setBrewTime(int brewTime) {
        this.brewTime = brewTime;
    }

    public void setBrewingMethod(String brewingMethod) {
        this.brewingMethod = brewingMethod;
    }

    public void setBloomTime(int bloomTime) {
        this.bloomTime = bloomTime;
    }

    public void setBloomWater(int bloomWater) {
        this.bloomWater = bloomWater;
    }

    public void setCoffeeAmount(int coffeeAmount) {
        this.coffeeAmount = coffeeAmount;
    }

    public void setBrewWaterAmount(int brewWaterAmount) {
        this.brewWaterAmount = brewWaterAmount;
    }
}
