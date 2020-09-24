package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.R;

import static android.icu.lang.UProperty.INT_START;

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
        private TextView upsideDownMethodText;

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
            this.upsideDownMethodText = v.findViewById(R.id.upsideDownMethodText);
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



        final String Text = "Heat " + waterAmount + "g of water" + " to " + temp + " °C.";

        SpannableString TempWater = new SpannableString(Text);
        TempWater.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 6+Integer.toString(waterAmount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TempWater.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                6+Integer.toString(waterAmount).length()+13,
                6+Integer.toString(waterAmount).length()+13+Integer.toString(temp).length()+3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        recipeViewHolder.tempItem.setText(TempWater);

        final String Text1 = "Grind " + coffeeAmount + "g of coffee" + " to a " + groundSize + " grind.";
        SpannableString CoffeeGrind = new SpannableString(Text1);

        CoffeeGrind.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 6, 7+ Integer.toString(coffeeAmount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        if (brewingMethod.equals("Standard"))
            recipeViewHolder.brewingMethodItem.setText("Place the aeropress on the mug in the normal orientation with wet filter and cap on.");
        else{
            recipeViewHolder.brewingMethodItem.setText("Place the aeropress in the upside-down orientation.");
            recipeViewHolder.upsideDownMethodText.setVisibility(View.VISIBLE);
        }

        recipeViewHolder.groundSizeItem.setText(CoffeeGrind);

        if (bloomTime!=0){
            // BREWING WITH BLOOM
            final String bloomText = ("Add " + bloomWater + "g of water and wait " + bloomTime + " seconds for the coffee to bloom.");

            SpannableString Bloom = new SpannableString(bloomText);
            Bloom.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 4, 5+Integer.toString(bloomWater).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Bloom.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                    23+Integer.toString(bloomWater).length(),
                    26+Integer.toString(bloomWater).length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            recipeViewHolder.bloomTimeItem.setText(Bloom);

            final String remainingWater = "Add the remaining " + Integer.toString(waterAmount-bloomWater) + "g of water.";

            SpannableStringBuilder water = new SpannableStringBuilder(remainingWater);
            water.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 17, 19+Integer.toString(waterAmount-bloomWater).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            recipeViewHolder.bloomWaterItem.setText(water);
        }
        else{
            // NO BLOOM.
            recipeViewHolder.bloomTimeItem.setVisibility(View.GONE);
            final String remainingWater = "Add " + Integer.toString(waterAmount) + "g of water slowly.";

            SpannableStringBuilder water = new SpannableStringBuilder(remainingWater);
            water.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 4, 5+Integer.toString(waterAmount-bloomWater).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            recipeViewHolder.bloomWaterItem.setText(water);
        }

        final String timeToBrew = "Wait " + brewTime + "s to brew.";
        SpannableStringBuilder spannableTimeToBrew = new SpannableStringBuilder(timeToBrew);
        spannableTimeToBrew.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, Integer.toString(brewTime).length()+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        recipeViewHolder.brewTimeItem.setText(spannableTimeToBrew);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
