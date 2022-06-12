package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.os.Bundle;


import aeropresscipe.divinelink.aeropress.base.HomeView;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import aeropresscipe.divinelink.aeropress.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SavedRecipesFragment extends Fragment implements SavedRecipesView {

    private SavedRecipesPresenter presenter;
    private HomeView homeView;

    private RecyclerView savedRecipesRV;
    private Toolbar mToolBar;
    private LinearLayout mEmptyListLL;

    private Animation mFadeAnimation;

    @Nullable private SavedRecipesAdapter recipesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        if (getArguments() != null) {
            homeView = (HomeView) getArguments().getSerializable("home_view");
        }

        recipesAdapter = new SavedRecipesAdapter(requireContext(),
            new SavedRecipesAdapterDelegate() {
                @Override
                public void brewItem(int position) {
                    presenter.getSpecificRecipeToStartNewBrew(requireContext(), position);
                }

                @Override
                public void deleteItem(@NotNull SavedRecipeDomain recipe, int position) {
                    presenter.deleteRecipe(recipe, requireContext(), position);
                }
            }
        );

        savedRecipesRV = (RecyclerView) v.findViewById(R.id.savedRecipesRV);
        mToolBar = (Toolbar) v.findViewById(R.id.toolbar);
        mEmptyListLL = (LinearLayout) v.findViewById(R.id.emptyListLayout);

        mToolBar.setNavigationOnClickListener(v1 -> getActivity().onBackPressed());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        savedRecipesRV.setLayoutManager(layoutManager);

        presenter = new SavedRecipesPresenterImpl(this);

        presenter.getSavedRecipes(getContext());
        return v;
    }


    public static SavedRecipesFragment newInstance(HomeView homeView) {

        SavedRecipesFragment fragment = new SavedRecipesFragment();
        Bundle args = new Bundle();
        args.putSerializable("home_view", homeView);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void showSavedRecipes(final List<SavedRecipeDomain> savedRecipes) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                mFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_favourites);
                savedRecipesRV.setAnimation(mFadeAnimation);
                savedRecipesRV.setAdapter(recipesAdapter);
                if (recipesAdapter != null) {
                    recipesAdapter.submitList(savedRecipes);
                }
                recipesAdapter.createSwipeHelper(savedRecipesRV);
            });
        }
    }

    @Override
    public void showSavedRecipesAfterDeletion(final List<SavedRecipeDomain> savedRecipes, final int position) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (recipesAdapter != null) {
                    recipesAdapter.submitList(savedRecipes);
                }
            });
        }
    }

    @Override
    public void passData(final int bloomTime, final int brewTime, final int bloomWater, final int brewWater) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                DiceUI diceUI = new DiceUI(bloomTime, brewTime, bloomWater, brewWater);
                diceUI.setNewRecipe(true);
                homeView.startTimerActivity(diceUI);
            });
        }
    }

    @Override
    public void showEmptyListMessage() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                beginFading(savedRecipesRV, AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_favourites), 8);
                beginFading(mEmptyListLL, AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_favourites), 0);
            });
        }
    }

    public void beginFading(View view, Animation animation, int visibility) {
        // 0 = View.VISIBLE, 4 = View.INVISIBLE, 8 = View.GONE
        view.startAnimation(animation);
        view.setVisibility(visibility);
    }
}
