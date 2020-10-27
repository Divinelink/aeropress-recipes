package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SavedRecipesRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//public class SavedRecipesRvAdapter extends RecyclerView.Adapter<SavedRecipesRvAdapter.SavedRecipeViewHolder> {

    private List<SavedRecipeDomain> savedRecipes;


    private Context context;

    private ItemTouchHelper mItemTouchHelper;

    public static final int ITEM_TYPE_RECYCLER_WIDTH = 1000;
    public static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    public static final int ITEM_TYPE_ACTION_WIDTH_NO_SPRING = 1002;
    public static final int ITEM_TYPE_NO_SWIPE = 1003;

  //  private ItemTouchHelperExtension mItemTouchHelperExtension;
  private LayoutInflater getLayoutInflater() {
      return LayoutInflater.from(context);
  }
    public SavedRecipesRvAdapter(List<SavedRecipeDomain> savedRecipes, Context context, ItemTouchHelper mItemTouchHelper) {
        this.savedRecipes = savedRecipes;
        this.context = context;
        this.mItemTouchHelper = mItemTouchHelper;
    }



    class SavedRecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView waterAndTempItem;
        private TextView beansWeightItem;
        private TextView beansGrindLevelItem;
        private TextView brewingMethodItem;
        private TextView timeItem;
        private TextView brewedOnItem;

        View mViewContent;
        View mActionContainer;


        public SavedRecipeViewHolder(View v) {
            super(v);

            this.waterAndTempItem = v.findViewById(R.id.waterAndTempTV);
            this.beansWeightItem = v.findViewById(R.id.beansWeightTV);
            this.beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV);
            this.brewingMethodItem = v.findViewById(R.id.brewMethodTV);
            this.timeItem = v.findViewById(R.id.savedTimeTV);
            this.brewedOnItem = v.findViewById(R.id.brewedOnTV);

            mViewContent = v.findViewById(R.id.saved_recipe_item);
            mActionContainer = v.findViewById(R.id.view_list_repo_action_container);

        }

        public void bind() {

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {

                       mItemTouchHelper.startDrag(SavedRecipeViewHolder.this);


                    }
                    return true;
                }
            });
        }



    }

    @NonNull
    @Override
    public SavedRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      View v = getLayoutInflater().inflate(R.layout.list_item_main, viewGroup, false);
    //    View v = LayoutInflater.from(viewGroup.getContext())
      //          .inflate(R.layout.list_item_main, viewGroup, false);
     //   SavedRecipeViewHolder vh = new SavedRecipeViewHolder(v);
        return new ItemSwipeWithActionWidthViewHolder(v);
        /*
        if (i == ITEM_TYPE_ACTION_WIDTH) return new ItemSwipeWithActionWidthViewHolder(v);
        if (i == ITEM_TYPE_NO_SWIPE) return new ItemNoSwipeViewHolder(v);
        if (i == ITEM_TYPE_RECYCLER_WIDTH) {
            v = getLayoutInflater().inflate(R.layout.list_item_with_single_delete, viewGroup, false);
            return new ItemViewHolderWithRecyclerWidth(v);
        }

        return new ItemSwipeWithActionWidthNoSpringViewHolder(v);
        */

    //    return vh;
    }

    @Override
    //public void onBindViewHolder(@NonNull final SavedRecipeViewHolder savedRecipeViewHolder, int i) {
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int i) {
        SavedRecipeViewHolder savedRecipeViewHolder = (SavedRecipeViewHolder) holder;
        savedRecipeViewHolder.bind();
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

        // Needed for Swipe Thingy //

   //     SavedRecipeViewHolder baseViewHolder = (SavedRecipeViewHolder) savedRecipeViewHolder;
    //    baseViewHolder.bind(savedRecipes.get(position));

        savedRecipeViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Item Content click: #" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });



     /*   if (holder instanceof ItemViewHolderWithRecyclerWidth) {
            ItemViewHolderWithRecyclerWidth viewHolder = (ItemViewHolderWithRecyclerWidth) holder;
            viewHolder.mActionViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doDelete(holder.getAdapterPosition());
                }
            });

      */
  //     } else if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
        if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewRefresh.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "Refresh Click" + holder.getAdapterPosition()
                                    , Toast.LENGTH_SHORT).show();

                        }
                    }

            );
            viewHolder.mActionViewDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            doDelete(holder.getAdapterPosition());
                        }
                    }

            );
        }

    }



    @Override
    public int getItemCount() {
        return savedRecipes.size();
    }

    // NEEDED FOR SWIPE THINGY //

    public void move(int from, int to) {
        notifyItemMoved(from, to);
    }

    public void setDatas(List<SavedRecipeDomain> datas) {
        savedRecipes.clear();
        savedRecipes.addAll(datas);
    }

    public void updateData(List<SavedRecipeDomain> datas) {
        setDatas(datas);
        notifyDataSetChanged();
    }

    private void doDelete(int adapterPosition) {
        savedRecipes.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    class ItemViewHolderWithRecyclerWidth extends SavedRecipeViewHolder {

        View mActionViewDelete;

        public ItemViewHolderWithRecyclerWidth(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
        }

    }

    class ItemSwipeWithActionWidthViewHolder extends SavedRecipeViewHolder {

        View mActionViewDelete;
        View mActionViewRefresh;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.view_list_repo_action_delete);
            mActionViewRefresh = itemView.findViewById(R.id.view_list_repo_action_update);
        }


        public float getActionWidth() {
            return mActionContainer.getWidth();
        }


    }

    class ItemNoSwipeViewHolder extends SavedRecipeViewHolder {

        public ItemNoSwipeViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

}
