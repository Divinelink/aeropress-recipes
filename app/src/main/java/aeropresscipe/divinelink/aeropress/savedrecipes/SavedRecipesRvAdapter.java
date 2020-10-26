package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesRvAdapter extends RecyclerView.Adapter<SavedRecipesRvAdapter.SavedRecipeViewHolder> {

    private List<SavedRecipeDomain> savedRecipes;

    private Context context;

    public SavedRecipesRvAdapter(List<SavedRecipeDomain> savedRecipes, Context context) {
        this.savedRecipes = savedRecipes;
        this.context = context;
    }

    public static class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView waterAndTempItem;
        private TextView beansWeightItem;
        private TextView beansGrindLevelItem;
        private TextView brewingMethodItem;
        private TextView timeItem;
        private TextView brewedOnItem;


        public SavedRecipeViewHolder(View v) {
            super(v);

            this.waterAndTempItem = v.findViewById(R.id.waterAndTempTV);
            this.beansWeightItem = v.findViewById(R.id.beansWeightTV);
            this.beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV);
            this.brewingMethodItem = v.findViewById(R.id.brewMethodTV);
            this.timeItem = v.findViewById(R.id.savedTimeTV);
            this.brewedOnItem = v.findViewById(R.id.brewedOnTV);

        }
    }

    @NonNull
    @Override
    public SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_recipe_item, viewGroup, false);
        SavedRecipeViewHolder vh = new SavedRecipeViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeViewHolder savedRecipeViewHolder, int i) {
        final int position = i;

        final int total_water = savedRecipes.get(position).getBloomWater() + savedRecipes.get(position).getBrewWaterAmount();
        final int total_time = savedRecipes.get(position).getBloomTime() + savedRecipes.get(position).getBrewTime();
        final int bloomTime = savedRecipes.get(position).getBloomTime();
        final int temp = savedRecipes.get(position).getDiceTemperature();
        final String grindSize = savedRecipes.get(position).getGroundSize().substring(0,1).toUpperCase() + savedRecipes.get(position).getGroundSize().substring(1).toLowerCase();

        savedRecipeViewHolder.waterAndTempItem.setText(context.getResources().getString(R.string.SavedWaterAndTempTextView, total_water, temp, temp * 9 / 5 + 32));
        savedRecipeViewHolder.beansWeightItem.setText(context.getResources().getString(R.string.SavedCoffeeWeightTextView, savedRecipes.get(position).getCoffeeAmount()));



        savedRecipeViewHolder.beansGrindLevelItem.setText(context.getResources().getString(R.string.SavedGrindLevelTextView, grindSize));
        savedRecipeViewHolder.brewingMethodItem.setText(context.getResources().getString(R.string.SavedBrewingMethodTextView, savedRecipes.get(position).getBrewingMethod()));

        if (bloomTime == 0)
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeTextView, total_time));
        else
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeWithBloomTextView, savedRecipes.get(position).getBrewTime(), bloomTime));

        savedRecipeViewHolder.brewedOnItem.setText(context.getResources().getString(R.string.dateBrewedTextView, savedRecipes.get(position).getDateBrewed()));


    }

    @Override
    public int getItemCount() {
        return savedRecipes.size();
    }


}
