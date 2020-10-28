package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.graphics.Color;
import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.features.SwipeHelper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SavedRecipesFragment extends Fragment implements SavedRecipesView{


    private SavedRecipesPresenter presenter;

    private RecyclerView savedRecipesRV;
    private Toolbar myToolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        savedRecipesRV = (RecyclerView) v.findViewById(R.id.savedRecipesRV);
        myToolbar = (Toolbar) v.findViewById(R.id.toolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        savedRecipesRV.setLayoutManager(layoutManager);

        //TODO ADD SLIDE DELETE ACTION AND BREW ON RECYCLE VIEW


        presenter = new SavedRecipesPresenterImpl(this);

        presenter.getSavedRecipes(getContext());

        createSwipeHelper();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SavedRecipesFragment newInstance() {

        SavedRecipesFragment fragment = new SavedRecipesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showSavedRecipes(final List<SavedRecipeDomain> savedRecipes) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                SavedRecipesRvAdapter savedRecipesRvAdapter = new SavedRecipesRvAdapter(savedRecipes, getActivity());

                @Override
                public void run() {
                    savedRecipesRV.setAdapter(savedRecipesRvAdapter);

                }
            });
        }
    }

    public void createSwipeHelper() {

        //TODO Fix Height of Buttons
        //TODO Make it not close swipes when opening another cardView
        //TODO Edit Button click events

        SwipeHelper swipeHelper = new SwipeHelper(getContext(), savedRecipesRV) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        ContextCompat.getColor(getContext(),R.color.red),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: onDelete
                            }
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Brew",
                        0,
                        ContextCompat.getColor(getContext(),R.color.green),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                // TODO: OnTransfer
                            }
                        }
                ));
            }
        };

        swipeHelper.attachSwipe(getContext());
    }




}
