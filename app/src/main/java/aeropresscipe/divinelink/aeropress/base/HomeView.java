package aeropresscipe.divinelink.aeropress.base;

import java.io.Serializable;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;

public interface HomeView extends Serializable {

    void addTimerFragment(DiceUI diceUI);

}
