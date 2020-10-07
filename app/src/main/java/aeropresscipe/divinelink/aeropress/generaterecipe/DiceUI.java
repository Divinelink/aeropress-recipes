package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Parcel;
import android.os.Parcelable;

public class DiceUI implements Parcelable {

    private int bloomTime, brewTime;


    public DiceUI(int bloomTime, int brewTime) {
        this.bloomTime = bloomTime;
        this.brewTime = brewTime;
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
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

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
