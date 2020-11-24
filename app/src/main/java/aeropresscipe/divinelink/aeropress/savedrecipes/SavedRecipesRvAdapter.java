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
import aeropresscipe.divinelink.aeropress.features.SwipeHelper;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private List<SavedRecipeDomain> savedRecipes;
    final private Context context;
    final private RecyclerView recyclerView;

    private SavedRecipesPresenter presenter;
    private int cardViewMarginAttr;

    public SavedRecipesRvAdapter(List<SavedRecipeDomain> savedRecipes, Context context, RecyclerView recyclerView) {
        this.savedRecipes = savedRecipes;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void setPresenter(SavedRecipesPresenter presenter) {
        this.presenter = presenter;
    }

    static class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        final private TextView waterAndTempItem;
        final private TextView beansWeightItem;
        final private TextView beansGrindLevelItem;
        final private TextView brewingMethodItem;
        final private TextView timeItem;
        final private TextView brewedOnItem;
        final private CardView cardView;

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
        cardViewMarginAttr = lp.bottomMargin;

        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        SavedRecipeViewHolder savedRecipeViewHolder = (SavedRecipeViewHolder) holder;

        final int total_water = savedRecipes.get(i).getBrewWaterAmount();
        final int total_time = savedRecipes.get(i).getBloomTime() + savedRecipes.get(i).getBrewTime();
        final int bloomTime = savedRecipes.get(i).getBloomTime();
        final int temp = savedRecipes.get(i).getDiceTemperature();
        final String grindSize = savedRecipes.get(i).getGroundSize().substring(0, 1).toUpperCase() + savedRecipes.get(i).getGroundSize().substring(1).toLowerCase();

        savedRecipeViewHolder.waterAndTempItem.setText(context.getResources().getString(R.string.SavedWaterAndTempTextView, total_water, temp, temp * 9 / 5 + 32));
        savedRecipeViewHolder.beansWeightItem.setText(context.getResources().getString(R.string.SavedCoffeeWeightTextView, savedRecipes.get(i).getCoffeeAmount()));
        savedRecipeViewHolder.beansGrindLevelItem.setText(context.getResources().getString(R.string.SavedGrindLevelTextView, grindSize));
        savedRecipeViewHolder.brewingMethodItem.setText(context.getResources().getString(R.string.SavedBrewingMethodTextView, savedRecipes.get(i).getBrewingMethod()));

        if (bloomTime == 0)
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeTextView, total_time));
        else
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeWithBloomTextView, savedRecipes.get(i).getBrewTime(), bloomTime));

        savedRecipeViewHolder.brewedOnItem.setText(context.getResources().getString(R.string.dateBrewedTextView, savedRecipes.get(i).getDateBrewed()));

    }


    @Override
    public int getItemCount() {
        return savedRecipes.size();
    }


    public void createSwipeHelper() {

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
                        cardViewMarginAttr
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Brew",
                        0,
                        ContextCompat.getColor(context, R.color.green),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                presenter.getSpecificRecipeToStartNewBrew(context, pos);
                            }
                        },
                        cardViewMarginAttr
                ));
            }
        };
        swipeHelper.attachSwipe(context);
    }


    public void showDeleteRecipeDialog(final int position) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.deleteRecipeDialogTitle)
                .setMessage(R.string.deleteRecipeDialogMessage)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        presenter.deleteRecipe(savedRecipes.get(position), context, position);
                        savedRecipes.remove(position);
                        notifyItemRemoved(position);

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
