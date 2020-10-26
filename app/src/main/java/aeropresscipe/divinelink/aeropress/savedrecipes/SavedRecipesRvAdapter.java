package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.generaterecipe.GenerateRecipeRvAdapter;
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


        public SavedRecipeViewHolder(View v) {
            super(v);

            this.waterAndTempItem = v.findViewById(R.id.waterAndTempTV);
            this.beansWeightItem = v.findViewById(R.id.beansWeightTV);
            this.beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV);
            this.brewingMethodItem = v.findViewById(R.id.brewMethodTV);
            this.timeItem = v.findViewById(R.id.timeTV);


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


    }

    @Override
    public int getItemCount() {
        return savedRecipes.size();
    }


}
