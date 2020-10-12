package aeropresscipe.divinelink.aeropress.timer;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aeropresscipe.divinelink.aeropress.R;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class TimerFragment extends Fragment implements TimerView {


    TextView timerTextView;
    TextView notificationTextView;
    MaterialProgressBar progressBar;

    private TimerPresenter presenter;

    private int secondsRemaining = 0;

    DiceUI diceUI;

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
        presenter.getNumbersForTimer(diceUI.getBloomTime(), true);

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
        //TODO make it show both time phase and how much water we need to put into each phase
        if (bloomPhase) {
            progressBar.setMax(time);
            notificationTextView.setText(R.string.bloomPhase);
            timerHandler.postDelayed(bloomRunnable, 0);
        } else {
            timerHandler.postDelayed(brewRunnable, 0);
            notificationTextView.setText(R.string.brewPhase);
            progressBar.setMax(time-1);
        }
    }

    Handler timerHandler = new Handler();
    Runnable bloomRunnable = new Runnable() {
        @Override
        public void run() {

            updateCountdownUI();

            if (secondsRemaining == -1) {
                presenter.getNumbersForTimer(diceUI.getBrewTime()+1, false);
                timerHandler.removeCallbacks(bloomRunnable);
            } else {
                timerHandler.postDelayed(this, 1000);
            }

            secondsRemaining -= 1;
        }
    };

    Runnable brewRunnable = new Runnable() {
        @Override
        public void run() {
            updateCountdownUI();

            if (secondsRemaining == -1) {
                timerHandler.removeCallbacks(brewRunnable);
                //TODO ADD ANIMATION
                presenter.showMessage(getString(R.string.enjoyCoffee));
            } else {
                timerHandler.postDelayed(this, 1000);
            }

            secondsRemaining -= 1;
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
}
