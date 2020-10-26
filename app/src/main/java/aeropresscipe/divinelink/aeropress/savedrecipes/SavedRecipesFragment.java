package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.os.Bundle;

import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRvAdapter;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.recyclerview.widget.RecyclerView;


public class SavedRecipesFragment extends Fragment implements SavedRecipesView{


    private SavedRecipesPresenter presenter;

    private RecyclerView savedRecipesRV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        savedRecipesRV = (RecyclerView) v.findViewById(R.id.savedRecipesRV);


        presenter = new SavedRecipesPresenterImpl(this);

        presenter.getSavedRecipes(getContext());

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SavedRecipesFragment newInstance() {

        SavedRecipesFragment fragment = new SavedRecipesFragment();
        Bundle args = new Bundle();
      //  args.putParcelable("timer", diceUI);
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
}
