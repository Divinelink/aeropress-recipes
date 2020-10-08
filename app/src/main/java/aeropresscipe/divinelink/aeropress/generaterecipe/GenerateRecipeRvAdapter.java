package aeropresscipe.divinelink.aeropress.generaterecipe;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import aeropresscipe.divinelink.aeropress.R;


public class GenerateRecipeRvAdapter extends RecyclerView.Adapter<GenerateRecipeRvAdapter.RecipeViewHolder>{

    private int temp;
    private String groundSize;
    private int brewTime;
    private String brewingMethod;
    private int bloomTime;
    private int bloomWater;
    private int waterAmount;
    private int coffeeAmount;

    private Context context;

    public GenerateRecipeRvAdapter(int temp,
                                   String groundSize,
                                   int brewTime,
                                   String brewingMethod,
                                   int bloomTime,
                                   int bloomWater,
                                   int waterAmount,
                                   int coffeeAmount,
                                   Context context) {
        this.temp = temp;
        this.groundSize = groundSize;
        this.brewTime = brewTime;
        this.brewingMethod = brewingMethod;
        this.bloomTime = bloomTime;
        this.bloomWater = bloomWater;
        this.waterAmount = waterAmount;
        this.coffeeAmount = coffeeAmount;
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

        final String heatWaterText = context.getResources().getString(R.string.heatWaterText, waterAmount, temp, temp*9/5 + 32);

        /*In API 24 and above, we can use <b> </b> format in strings.xml in order to bold the text
        instead of using SpannableString like below.
        https://developer.android.com/guide/topics/resources/string-resource
        But we'll keep on going with this solution for now.
        */
        SpannableString TempWater = new SpannableString(heatWaterText);
        TempWater.setSpan(
                new StyleSpan(Typeface.BOLD_ITALIC),
                5,
                6+Integer.toString(waterAmount).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TempWater.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                6+Integer.toString(waterAmount).length()+13,
                6+Integer.toString(waterAmount).length()+13+Integer.toString(temp).length()+3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TempWater.setSpan(new StyleSpan(Typeface.BOLD_ITALIC),
                6+Integer.toString(waterAmount).length()+13+Integer.toString(temp).length()+5,
                6+Integer.toString(waterAmount).length()+13+Integer.toString(temp).length()+10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        recipeViewHolder.tempItem.setText(TempWater);

        final String grindCoffeeText = context.getResources().getString(R.string.grindCoffeeText, coffeeAmount, groundSize);

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

        final int minutes = brewTime / 60;
        final int seconds = brewTime % 60;
        final String finalTime = String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);

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
