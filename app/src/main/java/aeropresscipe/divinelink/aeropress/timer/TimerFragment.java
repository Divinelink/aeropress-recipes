package aeropresscipe.divinelink.aeropress.timer;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import aeropresscipe.divinelink.aeropress.R;


public class TimerFragment extends Fragment implements TimerView {


    ProgressBar progressBar;
    TextView bloomTextView;
    TextView brewTextView;

    private TimerPresenter presenter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        presenter = new TimerPresenterImpl(this);
        presenter.getNumbersForTimer();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TimerFragment newInstance() {

        Bundle args = new Bundle();
        TimerFragment fragment = new TimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showTimer() {



    }
}