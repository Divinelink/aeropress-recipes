package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateRecipeFragment extends Fragment implements GenerateRecipeView {


    RecyclerView recipeRv;
    Button generateRecipeButton;

    private GenerateRecipePresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_generate_recipe, container, false);

        recipeRv = (RecyclerView) v.findViewById(R.id.recipe_rv);
        generateRecipeButton = v.findViewById(R.id.generateRecipeButton);

        generateRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getRecipe();
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
    public void showRecipe(ArrayList<DiceDomain> tempDice,
                           ArrayList<DiceDomain> groundSizeDice,
                           ArrayList<DiceDomain> brewingMethodDice,
                           ArrayList<DiceDomain> waterAmountDice) {

        GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter
                (tempDice,
                groundSizeDice,
                brewingMethodDice,
                waterAmountDice,
                getActivity());

        recipeRv.setAdapter(recipeRvAdapter);
    }
}