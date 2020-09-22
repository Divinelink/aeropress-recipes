package aeropresscipe.divinelink.aeropress.generaterecipe;

public class DiceDomain {

    private int diceID;
    private int diceTemperature;
    private String GroundSize;
    private int brewTime;
    private String brewingMethod;
    private int bloomTime;
    private int bloomWater;

    private int coffeeAmount;
    private int brewWaterAmount;




    public DiceDomain(int diceID, String groundSize, int brewTime) {
        this.diceID = diceID;
        this.GroundSize = groundSize;
        this.brewTime = brewTime;
    }

    public DiceDomain(int diceID, int coffeeAmount, int brewWaterAmount) {
        this.diceID = diceID;
        this.coffeeAmount = coffeeAmount;
        this.brewWaterAmount = brewWaterAmount;
    }

    public DiceDomain(int diceID, int diceTemperature) {
        this.diceID = diceID;
        this.diceTemperature = diceTemperature;
    }

    public DiceDomain(int diceID, String brewingMethod, int bloomTime, int bloomWater) {
        this.diceID = diceID;
        this.brewingMethod = brewingMethod;
        this.bloomWater = bloomWater;
    }
}
