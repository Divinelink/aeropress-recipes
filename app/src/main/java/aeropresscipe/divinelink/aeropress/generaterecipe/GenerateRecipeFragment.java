package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.base.HomeView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import aeropresscipe.divinelink.aeropress.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateRecipeFragment extends Fragment implements GenerateRecipeView {


    RecyclerView recipeRv;
    LinearLayout generateRecipeButton, timerButton;
    Button resumeBrew, favoritesBtn;

    private Animation myFadeInAnimation;
    private GenerateRecipePresenter presenter;
    HomeView homeView;
    DiceUI diceUI;


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
        resumeBrew = v.findViewById(R.id.resumeBrewButton);
        favoritesBtn = v.findViewById(R.id.showSavedRecipes);


        //TODO ADD FADE-IN ANIMATION WHEN GENERATING NEW RECIPE
        generateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getNewRecipe(getContext(), false);
            }
        });

        generateRecipeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                presenter.getNewRecipe(getContext(), true);
                return true;
            }
        });

        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceUI.setNewRecipe(true);
                homeView.addTimerFragment(diceUI);
            }
        });

        resumeBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceUI.setNewRecipe(false);
                homeView.addTimerFragment(diceUI);
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeView.addSavedRecipesFragment();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeRv.setLayoutManager(layoutManager);

        presenter = new GenerateRecipePresenterImpl(this);
        presenter.getRecipe(getContext());

        myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_out);


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
    public void showRecipe(final int temp,
                           final String groundSize,
                           final int brewTime,
                           final String brewingMethod,
                           final int bloomTime,
                           final int bloomWater,
                           final int waterAmount,
                           final int coffeeAmount) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter
                        (temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount, getActivity());

                @Override
                public void run() {
                    recipeRv.setAdapter(recipeRvAdapter);
                    if (!myFadeInAnimation.hasEnded()) {
                        resumeBrew.startAnimation(myFadeInAnimation);
                    }

                }
            });
        }
    }

    @Override
    public void passData(int bloomTime, int brewTime, int bloomWater, int remainingBrewWater) {
        // Set bloom time and brewtime. Needed for Timer
        diceUI = new DiceUI(bloomTime, brewTime, bloomWater, remainingBrewWater);
    }

    @Override
    public void showIsAlreadyBrewingDialog() {
        Toast.makeText(getActivity(), R.string.alreadyBrewingDialog, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecipeRemoveResume(final int temp,
                                       final String groundSize,
                                       final int brewTime,
                                       final String brewingMethod,
                                       final int bloomTime,
                                       final int bloomWater,
                                       final int waterAmount,
                                       final int coffeeAmount) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter
                        (temp, groundSize, brewTime, brewingMethod, bloomTime, bloomWater, waterAmount, coffeeAmount, getActivity());

                @Override
                public void run() {
                    recipeRv.setAdapter(recipeRvAdapter);
                    resumeBrew.setVisibility(View.GONE);
                    if (!myFadeInAnimation.hasEnded()) {
                        myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                        resumeBrew.startAnimation(myFadeInAnimation);
                    }
                }

            });
        }
    }


}