package aeropresscipe.divinelink.aeropress.savedrecipes;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import aeropresscipe.divinelink.aeropress.R;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SavedRecipesFragment extends Fragment implements SavedRecipesView{


    private SavedRecipesPresenter presenter;

    private RecyclerView savedRecipesRV;
    private Toolbar myToolbar;


    public ItemTouchHelper mItemTouchHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        savedRecipesRV = (RecyclerView) v.findViewById(R.id.savedRecipesRV);
        myToolbar = (Toolbar) v.findViewById(R.id.toolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        savedRecipesRV.setLayoutManager(layoutManager);

        //TODO ADD SLIDE DELETE ACTION AND BREW ON RECYCLE VIEW


        presenter = new SavedRecipesPresenterImpl(this);

        presenter.getSavedRecipes(getContext());



       // ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
      //  itemTouchHelper.attachToRecyclerView(savedRecipesRV);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(savedRecipesRV);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SavedRecipesFragment newInstance() {

        SavedRecipesFragment fragment = new SavedRecipesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showSavedRecipes(final List<SavedRecipeDomain> savedRecipes) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                SavedRecipesRvAdapter savedRecipesRvAdapter = new SavedRecipesRvAdapter(savedRecipes, getActivity(), mItemTouchHelper);

                @Override
                public void run() {
                    savedRecipesRV.setAdapter(savedRecipesRvAdapter);

                }
            });
        }
    }

    ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.START);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            SavedRecipesRvAdapter adapter = (SavedRecipesRvAdapter) recyclerView.getAdapter();
            adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }



        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (dY != 0 && dX == 0) super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            SavedRecipesRvAdapter.SavedRecipeViewHolder holder = (SavedRecipesRvAdapter.SavedRecipeViewHolder) viewHolder;
            if (viewHolder instanceof SavedRecipesRvAdapter.ItemSwipeWithActionWidthViewHolder) {
                if (dX < -holder.mActionContainer.getWidth()) {
                    dX = -holder.mActionContainer.getWidth();
                }
                holder.mViewContent.setTranslationX(dX);
                return;
            }
            if (viewHolder instanceof SavedRecipesRvAdapter.SavedRecipeViewHolder)
                holder.mViewContent.setTranslationX(dX);


        }

    /*
        @Override
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX , float dY,int actionState, boolean isCurrentlyActive){

            new RecyclerViewDecorator.Builder(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.design_default_color_background))

                    .addSwipeLeftActionIcon(R.drawable.ic_aeropressbyxnimrodx)
                    .addSwipeRightLabel("Remove")

                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftLabel("Brew")

                    .setSwipeLeftLabelColor(R.color.white)
                    .setSwipeRightLabelColor(R.color.white)

                    .create()
                    .decorate();



            super.onChildDraw(c, recyclerView, viewHolder, dX/4, dY, actionState, isCurrentlyActive);

        }

 */

    };






}
