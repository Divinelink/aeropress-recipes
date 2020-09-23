package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.R;

public class GenerateRecipeRvAdapter extends RecyclerView.Adapter<GenerateRecipeRvAdapter.RecipeViewHolder>{

    private ArrayList<DiceDomain> tempDice;
    private ArrayList<DiceDomain> groundSizeDice;
    private ArrayList<DiceDomain> brewingMethodDice;
    private ArrayList<DiceDomain> waterAmountDice;
    private Context context;

    public GenerateRecipeRvAdapter(ArrayList<DiceDomain> tempDice,
                                   ArrayList<DiceDomain> groundSizeDice,
                                   ArrayList<DiceDomain> brewingMethodDice,
                                   ArrayList<DiceDomain> waterAmountDice,
                                   Context context) {
        this.tempDice = tempDice;
        this.groundSizeDice = groundSizeDice;
        this.brewingMethodDice = brewingMethodDice;
        this.waterAmountDice = waterAmountDice;
        this.context = context;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView recipeItem;

        public RecipeViewHolder(View v){
            super(v);
           // this.recipeItem = v.findViewById(R.id.recipeItem);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_recipe_item, viewGroup, false);
        RecipeViewHolder vh = new RecipeViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {

        final int position = i;
        //dices.get(i).getDiceTemperature();

      //  recipeViewHolder.recipeItem.setText(dices.get(position).getDiceTemperature());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
