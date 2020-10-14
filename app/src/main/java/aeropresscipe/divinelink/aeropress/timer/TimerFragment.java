package aeropresscipe.divinelink.aeropress.timer;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aeropresscipe.divinelink.aeropress.R;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.content.Context.MODE_PRIVATE;


public class TimerFragment extends Fragment implements TimerView {


    //FIXME fix bug where if you press back button and there's already a timer
    // it crashes after timer edds, because there's no context attached.
    // re-attach same context if there's already one?

    TextView timerTextView;
    TextView notificationTextView;
    MaterialProgressBar progressBar;

    private TimerPresenter presenter;

    private int secondsRemaining = 0;

    DiceUI diceUI;

    GetPhaseFactory getPhaseFactory = new GetPhaseFactory();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        diceUI = getArguments().getParcelable("timer");

        timerTextView = (TextView) v.findViewById(R.id.timeTV);
        progressBar = (MaterialProgressBar) v.findViewById(R.id.progressBar);
        notificationTextView = (TextView) v.findViewById(R.id.notificationTV);


        presenter = new TimerPresenterImpl(this);

        presenter.getNumbersForTimer(
                getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getTime(),
                getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getPhase());

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TimerFragment newInstance(DiceUI diceUI) {

        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putParcelable("timer", diceUI);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showTimer(final int time, boolean bloomPhase) {
        secondsRemaining = time;

        if (bloomPhase) {
            timerHandler.postDelayed(bloomRunnable, 0);
            notificationTextView.setText(getString(R.string.bloomPhase, diceUI.getBloomWater()));
        } else {
            timerHandler.postDelayed(brewRunnable, 0);
            // Checks if there was a bloom or not, and set corresponding text on textView.
            if (getPhaseFactory.findPhase(diceUI.getBloomTime(), diceUI.getBrewTime()).getPhase())
                notificationTextView.setText(getString(R.string.brewPhaseWithBloom, diceUI.getRemainingBrewWater()));
            else
                notificationTextView.setText(getString(R.string.brewPhaseNoBloom, diceUI.getRemainingBrewWater()));
        }
        progressBar.setMax(time);
        ObjectAnimator.ofInt(progressBar, "progress", time)
                .setDuration(500)
                .start();
    }

    Handler timerHandler = new Handler();
    Runnable bloomRunnable = new Runnable() {
        @Override
        public void run() {
            updateCountdownUI();

            if (secondsRemaining == 0) {
                presenter.getNumbersForTimer(
                        getPhaseFactory.findPhase(0, diceUI.getBrewTime()).getTime(),
                        false);
                timerHandler.removeCallbacks(bloomRunnable);
            } else {
                timerHandler.postDelayed(this, 1000);
                secondsRemaining -= 1;
            }
        }
    };

    Runnable brewRunnable = new Runnable() {
        @Override
        public void run() {
            updateCountdownUI();

            if (secondsRemaining == 0) {
                timerHandler.removeCallbacks(brewRunnable);
                //TODO ADD ANIMATION
                presenter.showMessage(getString(R.string.enjoyCoffee));
            } else {
                timerHandler.postDelayed(this, 1000);
                secondsRemaining -= 1;
            }
        }
    };

    private void updateCountdownUI() {
        int minutesUntilFinished = secondsRemaining / 60;
        int secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60;
        timerTextView.setText(String.format("%d:%02d", minutesUntilFinished, secondsInMinuteUntilFinished));
        progressBar.setProgress(secondsInMinuteUntilFinished + minutesUntilFinished * 60);
    }

    @Override
    public void showMessage(String message) {
        timerTextView.setVisibility(View.INVISIBLE);
        notificationTextView.setText(message);
    }


    @Override
    public void onStop() {
        super.onStop();

        presenter.saveValuesOnPause(getContext(), secondsRemaining);

    }

    @Override
    public void onStart() {
        super.onStart();

        //FIXME ...
        presenter.returnValuesOnResume(getContext());

    }
}
