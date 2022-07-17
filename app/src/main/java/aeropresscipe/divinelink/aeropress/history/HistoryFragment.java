package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe;
import aeropresscipe.divinelink.aeropress.timer.TimerActivity;
import aeropresscipe.divinelink.aeropress.timer.TimerFlow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HistoryFragment extends Fragment implements IHistoryView, ISharedPrefHistoryManager {

    private IHistoryPresenter presenter;

    private RecyclerView historyRecipesRV;
    private Toolbar mToolBar;
    private LinearLayout mEmptyListLL;
    private Animation mFadeAnimation;

    private boolean mHistoryIsEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        historyRecipesRV = (RecyclerView) v.findViewById(R.id.historyRV);
        mToolBar = (Toolbar) v.findViewById(R.id.toolbar);
        mToolBar.setOnMenuItemClickListener(toolbarMenuClickListener);
        mEmptyListLL = (LinearLayout) v.findViewById(R.id.emptyListLayout);

        mToolBar.setNavigationOnClickListener(v1 -> getActivity().onBackPressed());


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        historyRecipesRV.setLayoutManager(layoutManager);

        presenter = new HistoryPresenterImpl(this);
        presenter.getHistoryRecipes(getContext());

        return v;
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showHistory(final List<History> savedRecipes) {


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

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {

                beginFading(historyRecipesRV, AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_favourites), 8);
                beginFading(mEmptyListLL, AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_favourites), 0);
                setIsHistoryEmptyBool(getContext(), true);

            });
        }
    }

    public void beginFading(View view, Animation animation, int visibility) {
        // 0 = View.VISIBLE, 4 = View.INVISIBLE, 8 = View.GONE
        view.startAnimation(animation);
        view.setVisibility(visibility);
    }

    @Override
    public void passData(Recipe recipe) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                recipe.setNewRecipe(true);
                startActivity(TimerActivity.newIntent(requireContext(), recipe, TimerFlow.START));
            });
        }
    }

    Toolbar.OnMenuItemClickListener toolbarMenuClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_history:

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    mHistoryIsEmpty = preferences.getBoolean("isHistoryEmpty", false);
                    //TODO Make button unavailable when History is empty.
                    if (!mHistoryIsEmpty)
                        showDeleteHistoryDialog();

                    return true;
                default:
                    return false;
            }
        }
    };

    public void showDeleteHistoryDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.action_delete)
                .setMessage(R.string.deleteHistoryMessage)
                .setPositiveButton(R.string.delete, (dialogInterface, i) -> presenter.clearHistory(getContext()))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                })
                .show();
    }

    @Override
    public void setIsHistoryEmptyBool(Context ctx, boolean bool) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isHistoryEmpty", bool);
        editor.apply();
    }

    @Override
    public void setRecipeLiked(final boolean isLiked, final Integer pos) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> historyRecipesRV.getAdapter().notifyItemChanged(pos, isLiked));
        }

    }
}
