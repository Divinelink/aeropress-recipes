package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        int randomTempIndex = (int) (Math.random() * tempDice.size());
        int randomGroundSizeIndex = (int) (Math.random() * groundSizeDice.size());
        int randombrewingMethodIndex = (int) (Math.random() * brewingMethodDice.size());
        int randomWaterAmountIndex = (int) (Math.random() * waterAmountDice.size());

        int temp = tempDice.get(randomTempIndex).getDiceTemperature();
        String groundSize = groundSizeDice.get(randomGroundSizeIndex).getGroundSize();
        int brewTime = groundSizeDice.get(randomGroundSizeIndex).getBrewTime();

        String brewingMethod = brewingMethodDice.get(randombrewingMethodIndex).getBrewingMethod();
        int bloomTime = brewingMethodDice.get(randombrewingMethodIndex).getBloomTime();
        int bloomWater = brewingMethodDice.get(randombrewingMethodIndex).getBloomWater();

        int waterAmount = waterAmountDice.get(randomWaterAmountIndex).getBrewWaterAmount();
        int coffeeAmount = waterAmountDice.get(randombrewingMethodIndex).getCoffeeAmount();

        recipeViewHolder.tempItem.setText("Temp is: " + temp);

        recipeViewHolder.groundSizeItem.setText("Ground size: " + groundSize);
        recipeViewHolder.brewTimeItem.setText("Brew Time: " + brewTime);

        recipeViewHolder.brewingMethodItem.setText("Method: " + brewingMethod);
        recipeViewHolder.bloomTimeItem.setText("Bloom Time: " + bloomTime);
        recipeViewHolder.bloomWaterItem.setText("Bloom Water " + bloomWater);

        recipeViewHolder.coffeeAmountItem.setText("Coffee amount " + coffeeAmount);
        recipeViewHolder.brewWaterAmountItem.setText("Water amount " + waterAmount);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
