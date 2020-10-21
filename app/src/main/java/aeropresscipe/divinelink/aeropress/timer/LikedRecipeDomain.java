package aeropresscipe.divinelink.aeropress.timer;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "LikedRecipes")
public class LikedRecipeDomain {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String groundSize, brewingMethod;
    private int diceTemperature, brewTime, bloomTime, bloomWater, coffeeAmount, brewWaterAmount;

    public LikedRecipeDomain(int diceTemperature,
                             String groundSize,
                             int brewTime,
                             String brewingMethod,
                             int bloomTime,
                             int bloomWater,
                             int brewWaterAmount,
                             int coffeeAmount) {
        this.diceTemperature = diceTemperature;
        this.groundSize = groundSize;
        this.brewTime = brewTime;
        this.brewingMethod = brewingMethod;
        this.bloomTime = bloomTime;
        this.bloomWater = bloomWater;
        this.brewWaterAmount = brewWaterAmount;
        this.coffeeAmount = coffeeAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrewWaterAmount() {
        return brewWaterAmount;
    }

    public int getDiceTemperature() {
        return diceTemperature;
    }

    public String getGroundSize() {
        return groundSize;
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
        this.groundSize = groundSize;
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
