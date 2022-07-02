package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;


import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.customviews.Notification;
import aeropresscipe.divinelink.aeropress.timer.TimerActivity;

public class GenerateRecipeFragment extends Fragment implements GenerateRecipeView {

    private RecyclerView recipeRv;
    private LinearLayout generateRecipeButton;
    private LinearLayout timerButton;
    private Button resumeBrewBtn;

    private Animation mFadeInAnimation;
    private Animation mAdapterAnimation;
    private GenerateRecipePresenter presenter;

    private Recipe recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generate_recipe, container, false);

        recipeRv = v.findViewById(R.id.recipe_rv);
        generateRecipeButton = v.findViewById(R.id.generateRecipeButton);
        timerButton = v.findViewById(R.id.startTimerButton);
        resumeBrewBtn = v.findViewById(R.id.resumeBrewButton);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeRv.setLayoutManager(layoutManager);

        presenter = new GenerateRecipePresenterImpl(this);
        presenter.getRecipe(getContext());
        mFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.initiliaze_animation);

        initListeners();

        return v;
    }

    private void initListeners() {
        generateRecipeButton.setOnClickListener(view -> presenter.getNewRecipe(getContext(), false));

        generateRecipeButton.setOnLongClickListener(view -> {
            presenter.getNewRecipe(getContext(), true);
            return true;
        });

        timerButton.setOnClickListener(view -> {
            recipe.setNewRecipe(true);
            startActivity(TimerActivity.newIntent(requireContext(), recipe));
        });

        resumeBrewBtn.setOnClickListener(view -> {
            recipe.setNewRecipe(false);
            startActivity(TimerActivity.newIntent(requireContext(), recipe));
        });
    }

    @Override
    public void showRecipe(final Recipe randomRecipe) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                final GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter(randomRecipe, getActivity());
                @Override
                public void run() {
                    recipeRv.setAdapter(recipeRvAdapter);
                    if (!mFadeInAnimation.hasEnded()) {
                        try {
                            //FIXME Maybe this isn't the best solution. Basically, there's a problem where if you're using Fragment Transition animation, and you also want to load the following animation on the button, app crashes.
                            //So I used this try catch method and app waits until UI loads.
                            //This happens because, getContext() is null since there are parts of the lifecycle where this will return null.
                            mFadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_out);
                        } catch (Exception ignore) {
                            // do nothing
                        }
                        resumeBrewBtn.startAnimation(mFadeInAnimation);
                    }
                }
            });
        }
    }

    @Override
    public void passData(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void showIsAlreadyBrewingDialog() {
        Notification.Companion.make(generateRecipeButton, R.string.alreadyBrewingDialog).setAnchorView(R.id.bottom_navigation).show();
    }

    @Override
    public void showRecipeRemoveResume(final Recipe randomRecipe) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                final GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter(randomRecipe, getActivity());

                @Override
                public void run() {
                    mAdapterAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.adapter_anim);
                    recipeRv.startAnimation(mAdapterAnimation);
                    // We need this so the adapter changes during the animation phase, and not before it.
                    Handler adapterHandler = new Handler(Looper.getMainLooper());
                    Runnable adapterRunnable = () -> recipeRv.setAdapter(recipeRvAdapter);

                    adapterHandler.postDelayed(adapterRunnable, mAdapterAnimation.getDuration());

                    if (!mFadeInAnimation.hasEnded()) {
                        mFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                        resumeBrewBtn.startAnimation(mFadeInAnimation);
                    }

                }
            });
        }
    }

    @Override
    public void showRecipeAppStarts(final Recipe randomRecipe) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                final GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter(randomRecipe, getActivity());
                @Override
                public void run() {
                    recipeRv.setAdapter(recipeRvAdapter);
                    if (mFadeInAnimation == null)
                        mFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.initiliaze_animation);
                    resumeBrewBtn.startAnimation(mFadeInAnimation);
                }
            });
        }
    }

    public static GenerateRecipeFragment newInstance() {
        Bundle args = new Bundle();
        GenerateRecipeFragment fragment = new GenerateRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}