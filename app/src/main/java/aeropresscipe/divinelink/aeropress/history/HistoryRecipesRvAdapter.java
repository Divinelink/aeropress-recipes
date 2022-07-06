package aeropresscipe.divinelink.aeropress.history;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import aeropresscipe.divinelink.aeropress.features.SwipeHelper;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryRecipesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private List<History> historyRecipes;
    final private Context context;
    final private RecyclerView recyclerView;

    private IHistoryPresenter presenter;

    public HistoryRecipesRvAdapter(List<History> historyRecipes, Context context, RecyclerView recyclerView) {
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
//        final private CardView cardView;
        final private ImageView likeRecipeBtn;
        final private LinearLayout likeRecipeLayout;

        public SavedRecipeViewHolder(View v) {
            super(v);
            this.waterAndTempItem = v.findViewById(R.id.waterAndTempTV);
            this.beansWeightItem = v.findViewById(R.id.beansWeightTV);
            this.beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV);
            this.brewingMethodItem = v.findViewById(R.id.brewingMethodTextView);
            this.timeItem = v.findViewById(R.id.brewingTimeTextView);
            this.brewedOnItem = v.findViewById(R.id.recipe_title);
//            this.cardView = v.findViewById(R.id.card_view);
            this.likeRecipeBtn = v.findViewById(R.id.likeRecipeButton);
            this.likeRecipeLayout = v.findViewById(R.id.likeRecipeLayout);
        }
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_card_item, viewGroup, false);
        final SavedRecipeViewHolder vh = new SavedRecipeViewHolder(v);

        vh.likeRecipeBtn.setOnClickListener(view -> presenter.addRecipeToFavourites(context, vh.getLayoutPosition(), historyRecipes.get(vh.getLayoutPosition()).getRecipe()));

        vh.likeRecipeBtn.setOnTouchListener(likeRecipeBtnTouchListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i, @NonNull List<Object> payloads) {

        SavedRecipeViewHolder savedRecipeViewHolder = (SavedRecipeViewHolder) holder;

        final int total_water = historyRecipes.get(i).getRecipe().getBrewWaterAmount();
        final int total_time = historyRecipes.get(i).getRecipe().getBloomTime() + historyRecipes.get(i).getRecipe().getBrewTime();
        final int bloomTime = historyRecipes.get(i).getRecipe().getBloomTime();
        final int temp = historyRecipes.get(i).getRecipe().getDiceTemperature();
        final String grindSize = historyRecipes.get(i).getRecipe().getGroundSize().substring(0, 1).toUpperCase() + historyRecipes.get(i).getRecipe().getGroundSize().substring(1).toLowerCase();
        boolean isLiked;

        savedRecipeViewHolder.waterAndTempItem.setText(context.getResources().getString(R.string.SavedWaterAndTempTextView, total_water, temp, temp * 9 / 5 + 32));
        savedRecipeViewHolder.beansWeightItem.setText(context.getResources().getString(R.string.SavedCoffeeWeightTextView, historyRecipes.get(i).getRecipe().getCoffeeAmount()));
        savedRecipeViewHolder.beansGrindLevelItem.setText(context.getResources().getString(R.string.SavedGrindLevelTextView, grindSize));
        savedRecipeViewHolder.brewingMethodItem.setText(context.getResources().getString(R.string.SavedBrewingMethodTextView, historyRecipes.get(i).getRecipe().getBrewingMethod()));

        if (bloomTime == 0)
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeTextView, total_time));
        else
            savedRecipeViewHolder.timeItem.setText(context.getResources().getString(R.string.SavedTotalTimeWithBloomTextView, historyRecipes.get(i).getRecipe().getBrewTime(), bloomTime));

        savedRecipeViewHolder.brewedOnItem.setText(context.getResources().getString(R.string.dateBrewedTextView, historyRecipes.get(i).getDateBrewed()));

        savedRecipeViewHolder.likeRecipeLayout.setVisibility(View.VISIBLE);

        // When we want to change some data in our recycle view items, we pass a Payload. This list can have many different properties.
        // In our situation, we only have a boolean which checks whether the recipe is liked or not, and changed the ImageButton resource correspondingly.
        if (payloads.size() > 0)
            isLiked = (boolean) payloads.get(0);
        else
            isLiked = historyRecipes.get(i).isRecipeLiked();

        if (isLiked)
            savedRecipeViewHolder.likeRecipeBtn.setImageResource(R.drawable.ic_heart_on);
        else
            savedRecipeViewHolder.likeRecipeBtn.setImageResource(R.drawable.ic_heart_off);

        super.onBindViewHolder(holder, i, payloads);
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
                        pos -> presenter.getSpecificRecipeToStartNewBrew(context, pos), 5
                ));
            }
        };
        swipeHelper.attachSwipe(context);
    }

    View.OnTouchListener likeRecipeBtnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                AnimatorSet reducer = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.reduce_size);
                reducer.setTarget(view);
                reducer.start();

            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                AnimatorSet regainer = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.regain_size);
                regainer.setTarget(view);
                regainer.start();
            }
            return false;
        }
    };

}
