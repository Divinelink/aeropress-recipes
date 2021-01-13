package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.features.SwipeHelper;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryRecipesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private List<HistoryDomain> historyRecipes;
    final private Context context;
    final private RecyclerView recyclerView;

    private IHistoryPresenter presenter;
    private int cardViewMarginAttr;

    public HistoryRecipesRvAdapter(List<HistoryDomain> historyRecipes, Context context, RecyclerView recyclerView) {
        this.historyRecipes = historyRecipes;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public void setPresenter(IHistoryPresenter presenter) {
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
        final private ImageButton likeRecipeBtn;
        final private LinearLayout likeRecipeLayout;

        public SavedRecipeViewHolder(View v) {
            super(v);
            this.waterAndTempItem = v.findViewById(R.id.waterAndTempTV);
            this.beansWeightItem = v.findViewById(R.id.beansWeightTV);
            this.beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV);
            this.brewingMethodItem = v.findViewById(R.id.brewMethodTV);
            this.timeItem = v.findViewById(R.id.savedTimeTV);
            this.brewedOnItem = v.findViewById(R.id.brewedOnTV);
            this.cardView = v.findViewById(R.id.card_view);
            this.likeRecipeBtn = v.findViewById(R.id.likeRecipeButton);
            this.likeRecipeLayout = v.findViewById(R.id.likeRecipeLayout);
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

        vh.likeRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addRecipeToFavourites(context);
            }
        });

        return vh;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        SavedRecipeViewHolder savedRecipeViewHolder = (SavedRecipeViewHolder) holder;

        final int total_water = historyRecipes.get(i).getBrewWaterAmount();
        final int total_time = historyRecipes.get(i).getBloomTime() + historyRecipes.get(i).getBrewTime();
        final int bloomTime = historyRecipes.get(i).getBloomTime();
        final int temp = historyRecipes.get(i).getDiceTemperature();
        final String grindSize = historyRecipes.get(i).getGroundSize().substring(0, 1).toUpperCase() + historyRecipes.get(i).getGroundSize().substring(1).toLowerCase();
        final boolean isRecipeLiked = historyRecipes.get(i).isRecipeLiked();

        savedRecipeViewHolder.waterAndTempItem.setText(context.getResources().getString(R.string.SavedWaterAndTempTextView, total_water, temp, temp * 9 / 5 + 32));
        savedRecipeViewHolder.beansWeightItem.setText(context.getResources().getString(R.string.SavedCoffeeWeightTextView, historyRecipes.get(i).getCoffeeAmount()));
        savedRecipeViewHolder.beansGrindLevelItem.setText(context.getResources().getString(R.string.SavedGrindLevelTextView, grindSize));
        savedRecipeViewHolder.brewingMethodItem.setText(context.getResources().getString(R.string.SavedBrewingMethodTextView, historyRecipes.get(i).getBrewingMethod()));

        if (bloomTime == 0)
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeTextView, total_time));
        else
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeWithBloomTextView, historyRecipes.get(i).getBrewTime(), bloomTime));

        savedRecipeViewHolder.brewedOnItem.setText(context.getResources().getString(R.string.dateBrewedTextView, historyRecipes.get(i).getDateBrewed()));

        savedRecipeViewHolder.likeRecipeLayout.setVisibility(View.VISIBLE);
        if (isRecipeLiked)
            savedRecipeViewHolder.likeRecipeBtn.setImageResource(R.drawable.ic_heart_on);
        else
            savedRecipeViewHolder.likeRecipeBtn.setImageResource(R.drawable.ic_heart_off);

    }


    @Override
    public int getItemCount() {
        return historyRecipes.size();
    }

    public void createSwipeHelper() {

        SwipeHelper swipeHelper = new SwipeHelper(context, recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

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



}
