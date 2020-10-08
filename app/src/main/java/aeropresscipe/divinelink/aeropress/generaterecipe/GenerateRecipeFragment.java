package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.base.HomeView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateRecipeFragment extends Fragment implements GenerateRecipeView {


    RecyclerView recipeRv;
    LinearLayout generateRecipeButton;
    Button timerButton;


    private GenerateRecipePresenter presenter;
    HomeView homeView;
    DiceUI diceUI;

    boolean firstTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_generate_recipe, container, false);

        homeView = (HomeView) getArguments().getSerializable("home_view");
        recipeRv = (RecyclerView) v.findViewById(R.id.recipe_rv);
        generateRecipeButton = v.findViewById(R.id.generateRecipeButton);
        //FIXME TEMPORARY BUTTON
        timerButton = v.findViewById(R.id.startTimerButton);
        //TODO ADD FADE-IN ANIMATION WHEN GENERATING NEW RECIPE
        generateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getNewRecipe(getContext());
            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // presenter.startTimer(timerUI, getActivity());
                homeView.addTimerFragment(diceUI);
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeRv.setLayoutManager(layoutManager);


        presenter = new GenerateRecipePresenterImpl(this);
        presenter.getRecipe(getContext());


        return v;
    }

    public static GenerateRecipeFragment newInstance(HomeView homeView) {

        Bundle args = new Bundle();
        GenerateRecipeFragment fragment = new GenerateRecipeFragment();
        args.putSerializable("home_view", homeView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showRecipe(final int temp, final String groundSize, final int brewTime, final String brewingMethod, final int bloomTime, final int bloomWater, final int waterAmount, final int coffeeAmount) {

        //FIXME Create a new object instead of this

        // Set bloom time and brewtime. Needed for Timer


        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter
                        (temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount, getActivity());

                @Override
                public void run() {
                    recipeRv.setAdapter(recipeRvAdapter);
                }
            });
        }
       // diceUI = new DiceUI(bloomTime, brewTime);

    }


    @Override
    public void passData(int bloomTime, int brewTime) {

           diceUI = new DiceUI(bloomTime, brewTime);
        // maybe we can pass data here instead of showRecipe, we'll see later

    }

}