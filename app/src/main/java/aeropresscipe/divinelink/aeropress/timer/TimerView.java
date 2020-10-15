package aeropresscipe.divinelink.aeropress.timer;

public interface TimerView {

    void showTimer(int bloomTime, boolean bloomPhase);
   //void showTimer(int bloomTime);

    void showMessage(String message);

    void showMessage();
}
