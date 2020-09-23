package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

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
        this.tempDice = tempDice; // Temp Dice only gives us the temp
        this.groundSizeDice = groundSizeDice; // Ground Size Dice gives us Ground Size and Brew Time
        this.brewingMethodDice = brewingMethodDice; // Brewing Method gives us Method, Bloom Time and Blood Water
        this.waterAmountDice = waterAmountDice; // Water Amount gives us Coffee amount and Water Amount
        this.context = context;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView tempItem;
        private TextView groundSizeItem;
        private TextView brewTimeItem;
        private TextView brewingMethodItem;
        private TextView bloomTimeItem;
        private TextView bloomWaterItem;
        private TextView coffeeAmountItem;
        private TextView brewWaterAmountItem;

        public RecipeViewHolder(View v){
            super(v);
            this.tempItem = v.findViewById(R.id.tempItem);
            this.groundSizeItem = v.findViewById(R.id.groundSizeItem);
            this.brewTimeItem = v.findViewById(R.id.brewTimeItem);
            this.brewingMethodItem = v.findViewById(R.id.brewingMethodItem);
            this.bloomTimeItem = v.findViewById(R.id.bloomTimeItem);
            this.bloomWaterItem = v.findViewById(R.id.bloomWaterItem);
            this.coffeeAmountItem = v.findViewById(R.id.coffeeAmountItem);
            this.brewWaterAmountItem = v.findViewById(R.id.brewWaterAmountItem);
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

        final DiceDomain temp = tempDice.get(position);
        final DiceDomain groundSize = groundSizeDice.get(position);
        final DiceDomain brewingMethod = brewingMethodDice.get(position);
        final DiceDomain waterAmount =  waterAmountDice.get(position);


        recipeViewHolder.tempItem.setText(String.format(Locale.US,"%d",temp.getDiceTemperature()));

        recipeViewHolder.groundSizeItem.setText(groundSize.getGroundSize());
        recipeViewHolder.brewTimeItem.setText(String.format(Locale.US,"%d",groundSize.getBrewTime()));

        recipeViewHolder.brewingMethodItem.setText(brewingMethod.getBrewingMethod());
        recipeViewHolder.bloomTimeItem.setText(String.format(Locale.US,"%d",brewingMethod.getBloomTime()));
        recipeViewHolder.bloomWaterItem.setText(String.format(Locale.US,"%d",brewingMethod.getBloomWater()));

        recipeViewHolder.coffeeAmountItem.setText(String.format(Locale.US,"%d",waterAmount.getCoffeeAmount()));
        recipeViewHolder.brewWaterAmountItem.setText(String.format(Locale.US,"%d",waterAmount.getBrewWaterAmount()));

    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
