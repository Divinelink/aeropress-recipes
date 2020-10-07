package aeropresscipe.divinelink.aeropress.timer;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeFragment;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import aeropresscipe.divinelink.aeropress.R;
import androidx.recyclerview.widget.RecyclerView;


public class TimerFragment extends Fragment implements TimerView {


    ProgressBar progressBar;
    TextView bloomTextView;
    TextView brewTextView;

    private TimerPresenter presenter;

    DiceUI diceUI;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        diceUI = getArguments().getParcelable("timer");

        bloomTextView = (TextView) v.findViewById(R.id.bloomtimeTV);

    //    bloomTextView.setText( diceUI.getBloomTime());
        Toast.makeText(getContext(), Integer.toString(diceUI.getBloomTime()), Toast.LENGTH_LONG).show();

        presenter = new TimerPresenterImpl(this);
        presenter.getNumbersForTimer();

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
    public void showTimer() {



    }
}