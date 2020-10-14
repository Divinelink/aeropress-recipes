package aeropresscipe.divinelink.aeropress.timer;


abstract class TimerNumbers {

    protected int timeForPhase;

    abstract int getTime();

    abstract boolean getPhase();

}

class BloomPhase extends TimerNumbers {

    public BloomPhase(int time) {
        this.timeForPhase = time;
    }

    @Override
    boolean getPhase() {
        return true;
    }

    @Override
    int getTime() {
        return timeForPhase;
    }

}

class BrewPhase extends TimerNumbers {

    public BrewPhase(int time) {
        this.timeForPhase = time;
    }

    @Override
    boolean getPhase() {
        return false;
    }

    @Override
    int getTime() {
        return timeForPhase;
    }


}

class GetPhaseFactory {

    public TimerNumbers findPhase(int timeForBloom, int timeForBrew) {
        if (timeForBloom == 0) {
            return new BrewPhase(timeForBrew);
        } else if (timeForBloom > 0) {
            return new BloomPhase(timeForBloom);
        }

        return null;
    }
}


