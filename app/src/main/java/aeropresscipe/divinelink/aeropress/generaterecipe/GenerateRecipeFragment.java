package aeropresscipe.divinelink.aeropress.generaterecipe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateRecipeFragment extends Fragment implements GenerateRecipeView {


    RecyclerView recipeRv;

    private GenerateRecipePresenter presenter;




    public GenerateRecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_generate_recipe, container, false);

      /*  TextView tv1 = (TextView) v.findViewById(R.id.textView);
        TextView tv2 = (TextView) v.findViewById(R.id.textView2);
        TextView tv3 = (TextView) v.findViewById(R.id.textView3);
        TextView tv4 = (TextView) v.findViewById(R.id.textView4); */

        RecyclerView recipeRv = (RecyclerView) v.findViewById(R.id.recipe_rv);
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

        //GenerateRecipeRvAdapter recipeRvAdapter = new GenerateRecipeRvAdapter()



    }
}