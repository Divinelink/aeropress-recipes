package aeropresscipe.divinelink.aeropress.history;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.base.HomeView;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain;
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipesFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.recyclerview.widget.RecyclerView;


public class HistoryFragment extends Fragment implements IHistoryView {

    private IHistoryPresenter presenter;
    private HomeView homeView;

    private RecyclerView historyRecipesRV;
    private Toolbar mToolBar;
    private LinearLayout mEmptyListLL;


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
    public void showHistory(List<DiceDomain> savedRecipes) {



    }

    @Override
    public void showEmptyListMessage() {

    }
}