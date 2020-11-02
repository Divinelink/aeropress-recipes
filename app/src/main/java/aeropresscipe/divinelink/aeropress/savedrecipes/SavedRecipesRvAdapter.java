package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.base.HomeView;
import aeropresscipe.divinelink.aeropress.features.SwipeHelper;
import aeropresscipe.divinelink.aeropress.generaterecipe.DiceUI;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesRvAdapter extends RecyclerView.Adapter<SavedRecipesRvAdapter.SavedRecipeViewHolder>{

    private List<SavedRecipeDomain> savedRecipes;

    private Context context;
    private RecyclerView recyclerView;

    SavedRecipesPresenter presenter;

    private int cardViewMarginForSwipe;


    public SavedRecipesRvAdapter(List<SavedRecipeDomain> savedRecipes, Context context, RecyclerView recyclerView) {
        this.savedRecipes = savedRecipes;
        this.context = context;
        this.recyclerView = recyclerView;
    }
    public void setPresenter(SavedRecipesPresenter presenter) {
        this.presenter = presenter;
    }

    public static class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView waterAndTempItem;
        private TextView beansWeightItem;
        private TextView beansGrindLevelItem;
        private TextView brewingMethodItem;
        private TextView timeItem;
        private TextView brewedOnItem;
        private CardView cardView;


        public SavedRecipeViewHolder(View v) {
            super(v);

            this.waterAndTempItem = v.findViewById(R.id.waterAndTempTV);
            this.beansWeightItem = v.findViewById(R.id.beansWeightTV);
            this.beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV);
            this.brewingMethodItem = v.findViewById(R.id.brewMethodTV);
            this.timeItem = v.findViewById(R.id.savedTimeTV);
            this.brewedOnItem = v.findViewById(R.id.brewedOnTV);
            this.cardView = v.findViewById(R.id.card_view);
        }
    }

    @NonNull
    @Override
    public SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_recipe_item, viewGroup, false);
        SavedRecipeViewHolder vh = new SavedRecipeViewHolder(v);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vh.cardView.getLayoutParams();
        cardViewMarginForSwipe = lp.bottomMargin;

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecipeViewHolder savedRecipeViewHolder, int i) {
        final int position = i;

        final int total_water = savedRecipes.get(position).getBloomWater() + savedRecipes.get(position).getBrewWaterAmount();
        final int total_time = savedRecipes.get(position).getBloomTime() + savedRecipes.get(position).getBrewTime();
        final int bloomTime = savedRecipes.get(position).getBloomTime();
        final int temp = savedRecipes.get(position).getDiceTemperature();
        final String grindSize = savedRecipes.get(position).getGroundSize().substring(0, 1).toUpperCase() + savedRecipes.get(position).getGroundSize().substring(1).toLowerCase();

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

    public void createSwipeHelper() {


        //TODO Make it not close swipes when opening another cardView
        //TODO Edit Button click events

        SwipeHelper swipeHelper = new SwipeHelper(context, recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        ContextCompat.getColor(context, R.color.red),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                //Delete selected item
                                showDeleteRecipeDialog(pos);
                            }
                        },
                        cardViewMarginForSwipe
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Brew",
                        0,
                        ContextCompat.getColor(context, R.color.green),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                //FIXME has a bug where sometimes it saved wrong data
                                presenter.getSpecificRecipeToStartNewBrew(context, pos);

                            }
                        },
                        cardViewMarginForSwipe
                ));
            }
        };
        swipeHelper.attachSwipe(context);
    }


    public void showDeleteRecipeDialog(final int position){
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.deleteRecipeDialogTitle)
                .setMessage(R.string.deleteRecipeDialogMessage)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteRecipe(savedRecipes.get(position), context);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

}
