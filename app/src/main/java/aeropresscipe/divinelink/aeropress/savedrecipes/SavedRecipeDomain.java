package aeropresscipe.divinelink.aeropress.savedrecipes;

import java.io.Serializable;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "SavedRecipes")
public class SavedRecipeDomain {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int diceTemperature, brewTime, bloomTime, bloomWater, coffeeAmount, brewWaterAmount;
    private String groundSize, brewingMethod;
    private String dateBrewed;

//FIXME figure out how to remove diceDomain field from DB
    private DiceDomain diceDomain;

    public SavedRecipeDomain(DiceDomain diceDomain, String dateBrewed) {
        this.diceDomain = diceDomain;
        this.id = diceDomain.getId();
        this.diceTemperature = diceDomain.getDiceTemperature();
        this.groundSize = diceDomain.getGroundSize();
        this.brewTime = diceDomain.getBrewTime();
        this.brewingMethod = diceDomain.getBrewingMethod();
        this.bloomTime = diceDomain.getBloomTime();
        this.bloomWater = diceDomain.getBloomWater();
        this.brewWaterAmount = diceDomain.getBrewWaterAmount();
        this.coffeeAmount = diceDomain.getCoffeeAmount();
        this.dateBrewed = dateBrewed;
    }

    @Ignore
    public SavedRecipeDomain(int id,
                             int diceTemperature,
                             String groundSize,
                             int brewTime,
                             String brewingMethod,
                             int bloomTime,
                             int bloomWater,
                             int brewWaterAmount,
                             int coffeeAmount,
                             String dateBrewed) {
        this.id = id;
        this.diceTemperature = diceTemperature;
        this.groundSize = groundSize;
        this.brewTime = brewTime;
        this.brewingMethod = brewingMethod;
        this.bloomTime = bloomTime;
        this.bloomWater = bloomWater;
        this.brewWaterAmount = brewWaterAmount;
        this.coffeeAmount = coffeeAmount;
        this.dateBrewed = dateBrewed;
    }

    public DiceDomain getDiceDomain() {
        return diceDomain;
    }

    public void setDiceDomain(DiceDomain diceDomain) {
        this.diceDomain = diceDomain;
    }

    public String getDateBrewed() {
        return dateBrewed;
    }

    public void setDateBrewed(String dateBrewed) {
        this.dateBrewed = dateBrewed;
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
