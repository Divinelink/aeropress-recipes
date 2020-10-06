package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.base.HomeView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_generate_recipe, container, false);

        recipeRv = (RecyclerView) v.findViewById(R.id.recipe_rv);
        generateRecipeButton = v.findViewById(R.id.generateRecipeButton);
        //FIXME TEMPORARY BUTTON
        timerButton = v.findViewById(R.id.startTimerButton);

        //TODO ADD FADE-IN ANIMATION WHEN GENERATING NEW RECIPE
        generateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getRecipe();
            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get BrewTime and BloomTime and pass them to TimerFragment
                final int bloomTime;
                final int brewTime;
             //   presenter.startTimer(getActivity(), bloomTime, brewTime);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeRv.setLayoutManager(layoutManager);

        presenter = new GenerateRecipePresenterImpl(this);
        presenter.getRecipe();

        return v;
    }

    public static GenerateRecipeFragment newInstance() {

        Bundle args = new Bundle();
        GenerateRecipeFragment fragment = new GenerateRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showRecipe(int temp, String groundSize, int brewTime, String brewingMethod, int bloomTime, int bloomWater, int waterAmount, int coffeeAmount) {

        GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter
                (temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount, getActivity());

        recipeRv.setAdapter(recipeRvAdapter);
    }

}