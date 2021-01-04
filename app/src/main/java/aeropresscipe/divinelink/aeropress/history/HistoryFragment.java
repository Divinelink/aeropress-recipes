package aeropresscipe.divinelink.aeropress.history;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.base.HomeView;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesRvAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HistoryFragment extends Fragment implements IHistoryView {

    private IHistoryPresenter presenter;
    private HomeView homeView;

    private RecyclerView historyRecipesRV;
    private Toolbar mToolBar;
    private LinearLayout mEmptyListLL;
    private Animation mFadeAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_history, container, false);

        homeView = (HomeView) getArguments().getSerializable("home_view");

        historyRecipesRV = (RecyclerView) v.findViewById(R.id.historyRV);
        mToolBar = (Toolbar) v.findViewById(R.id.toolbar);
        mEmptyListLL = (LinearLayout) v.findViewById(R.id.emptyListLayout);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        historyRecipesRV.setLayoutManager(layoutManager);

        presenter = new HistoryPresenterImpl(this);
        presenter.getHistoryRecipes(getContext());

        return v;
    }

    public static HistoryFragment newInstance(HomeView homeView) {

        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable("home_view", homeView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showHistory(final List<HistoryDomain> savedRecipes) {


        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                final HistoryRecipesRvAdapter historyRecipesRvAdapter = new HistoryRecipesRvAdapter(savedRecipes, getActivity(), historyRecipesRV);

                @Override
                public void run() {
                    mFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_favourites);
                    historyRecipesRV.setAnimation(mFadeAnimation);
                    historyRecipesRV.setAdapter(historyRecipesRvAdapter);
                    historyRecipesRvAdapter.createSwipeHelper();
                    historyRecipesRvAdapter.setPresenter(presenter);
                }
            });
        }
    }

    @Override
    public void showEmptyListMessage() {

        historyRecipesRV.setVisibility(View.GONE);
        mEmptyListLL.setVisibility(LinearLayout.VISIBLE);

    }
}