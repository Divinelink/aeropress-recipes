package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aeropresscipe.divinelink.aeropress.R;

import static android.text.Html.FROM_HTML_MODE_LEGACY;


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

        //TODO add Celcius to fahrenheit conversion
        final String heatWaterText = context.getResources().getString(R.string.heatWaterText, waterAmount, temp);

        /*In API 24 and above, we can use <b> </b> format in strings.xml in order to bold the text
        instead of using SpannableString like below.
        https://developer.android.com/guide/topics/resources/string-resource
        But we'll keep on going with this solution for now.
        */

        SpannableString TempWater = new SpannableString(heatWaterText);
        TempWater.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 6+Integer.toString(waterAmount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TempWater.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                6+Integer.toString(waterAmount).length()+13,
                6+Integer.toString(waterAmount).length()+13+Integer.toString(temp).length()+3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        recipeViewHolder.tempItem.setText(TempWater);

        final String grindCoffeeText = context.getResources().getString(R.string.grindCoffeeText, coffeeAmount, groundSize);

        //final String Text1 = "Grind " + coffeeAmount + "g of coffee" + " to a " + groundSize + " grind.";
        SpannableString CoffeeGrind = new SpannableString(grindCoffeeText);

        CoffeeGrind.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 6, 7+ Integer.toString(coffeeAmount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        if (brewingMethod.equals(context.getResources().getString(R.string.standard)))
            recipeViewHolder.brewingMethodItem.setText(R.string.normal_orientation_text);
        else{
            recipeViewHolder.brewingMethodItem.setText(R.string.inverted_orientation_text);
            recipeViewHolder.upsideDownMethodText.setVisibility(View.VISIBLE);
        }

        recipeViewHolder.groundSizeItem.setText(CoffeeGrind);

        if (bloomTime!=0){
            // BREWING WITH BLOOM
            final String bloomText = context.getResources().getString(R.string.addWaterText, bloomWater, bloomTime);

            SpannableString Bloom = new SpannableString(bloomText);
            Bloom.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 4, 5+Integer.toString(bloomWater).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Bloom.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                    23+Integer.toString(bloomWater).length(),
                    26+Integer.toString(bloomWater).length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            recipeViewHolder.bloomTimeItem.setText(Bloom);

            final String remainingWater = context.getResources().getString(R.string.addRemainingWater, (waterAmount-bloomWater));

            SpannableStringBuilder water = new SpannableStringBuilder(remainingWater);
            water.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 17, 19+Integer.toString(waterAmount-bloomWater).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            recipeViewHolder.bloomWaterItem.setText(water);
        }
        else{
            // NO BLOOM.
            recipeViewHolder.bloomTimeItem.setVisibility(View.GONE);
            final String remainingWater = context.getResources().getString(R.string.addWaterSlowly, waterAmount);

            SpannableStringBuilder water = new SpannableStringBuilder(remainingWater);
            water.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 4, 5+Integer.toString(waterAmount-bloomWater).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            recipeViewHolder.bloomWaterItem.setText(water);
        }
        //TODO make brewtime to show in minutes:seconds format, not just seconds
        final int minutes = brewTime / 60;
        final int seconds = brewTime % 60;
        final String time = "%1$02d";
        final String finalTime = String.format("%d:%02d", minutes, seconds);


        final String timeToBrew = context.getResources().getString(R.string.waitToBrewText, finalTime, (minutes == 1 && seconds == 0) ? "minute" : "minutes");

        SpannableStringBuilder spannableTimeToBrew = new SpannableStringBuilder(timeToBrew);
        spannableTimeToBrew.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, Integer.toString(brewTime).length()+7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        recipeViewHolder.brewTimeItem.setText(spannableTimeToBrew);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

}
