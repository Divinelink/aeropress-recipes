package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class DiceUI implements Parcelable {

    private int bloomTime, brewTime, bloomWater, remainingBrewWater;
    private boolean isNewRecipe, recipeHadBloom;

    public DiceUI(int bloomTime, int brewTime, int bloomWater, int remainingBrewWater) {
        this.bloomTime = bloomTime;
        this.brewTime = brewTime;
        this.bloomWater = bloomWater;
        this.remainingBrewWater = remainingBrewWater;
    }

    public int getBloomTime() {
        return bloomTime;
    }

    public void setBloomTime(int bloomTime) {
        this.bloomTime = bloomTime;
    }

    public int getBrewTime() {
        return brewTime;
    }

    public int getBloomWater() {
        return bloomWater;
    }

    public boolean isNewRecipe() {
        return isNewRecipe;
    }

    public void setNewRecipe(boolean newRecipe) {
        isNewRecipe = newRecipe;
    }

    public boolean recipeHadBloom() {
        return recipeHadBloom;
    }

    public void setRecipeHadBloom(boolean recipeHadBloom) {
        this.recipeHadBloom = recipeHadBloom;
    }

    public void setBloomWater(int bloomWater) {
        this.bloomWater = bloomWater;
    }

    public int getRemainingBrewWater() {
        return remainingBrewWater;
    }

    public void setRemainingBrewWater(int remainingBrewWater) {
        this.remainingBrewWater = remainingBrewWater;
    }

    public void setBrewTime(int brewTime) {
        this.brewTime = brewTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected DiceUI(Parcel in) {
        bloomTime = in.readInt();
        brewTime = in.readInt();
        bloomWater = in.readInt();
        remainingBrewWater = in.readInt();
        isNewRecipe = in.readInt() == 1;
        recipeHadBloom = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(bloomTime);
        parcel.writeInt(brewTime);
        parcel.writeInt(bloomWater);
        parcel.writeInt(remainingBrewWater);
        parcel.writeInt(isNewRecipe ? 1 : 0);
        parcel.writeInt(recipeHadBloom ? 1 : 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DiceUI> CREATOR = new Parcelable.Creator<DiceUI>() {
        @Override
        public DiceUI createFromParcel(Parcel in) {
            return new DiceUI(in);
        }

        @Override
        public DiceUI[] newArray(int size) {
            return new DiceUI[size];
        }
    };
}
