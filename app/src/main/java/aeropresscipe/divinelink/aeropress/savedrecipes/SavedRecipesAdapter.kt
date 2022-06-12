package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.RecipeCardItemBinding
import aeropresscipe.divinelink.aeropress.features.SwipeHelper
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

interface SavedRecipesAdapterDelegate {
    fun brewItem(position: Int)
    fun deleteItem(recipe: SavedRecipeDomain, position: Int)
}

class SavedRecipesAdapter(
    private val context: Context,
    private val delegate: SavedRecipesAdapterDelegate,
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = compareItems(oldItem, newItem)

        private fun compareItems(oldItem: Any, newItem: Any): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }
) {

    companion object {
        const val Type_Recipe = 0
        const val Type_History = 1
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is SavedRecipeDomain) {
            Type_Recipe
        } else {
            Type_History
        }
    }

    override fun submitList(list: List<Any>?) {
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Type_Recipe) {
            return RecipeViewHolder(
                RecipeCardItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        if (holder is RecipeViewHolder) {
            holder.updateView(item as SavedRecipeDomain)
        }

        holder.itemView.setOnClickListener {
//            delegate.onItemClick(item = item)
        }
    }

    inner class RecipeViewHolder(var binding: RecipeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun updateView(item: SavedRecipeDomain) {
            val totalWater = item.brewWaterAmount
            val totalTime = item.bloomTime + item.brewTime
            val bloomTime = item.bloomTime
            val temp = item.diceTemperature
            val grindSize = item.groundSize.substring(0, 1)
                .uppercase(Locale.getDefault()) + item.groundSize.substring(1).lowercase(
                Locale.getDefault()
            )
            binding.recipeTitle.text =
                context.resources.getString(R.string.dateBrewedTextView, item.dateBrewed)
            binding.waterAndTempTV.text = context.resources.getString(
                R.string.SavedWaterAndTempTextView,
                totalWater,
                temp,
                temp * 9 / 5 + 32
            )
            binding.beansWeightTV.text =
                context.resources.getString(R.string.SavedCoffeeWeightTextView, item.coffeeAmount)
            binding.beansGrindLevelTV.text =
                context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
            binding.brewingMethodTextView.text =
                context.resources.getString(R.string.SavedBrewingMethodTextView, item.brewingMethod)

            if (bloomTime == 0) {
                binding.brewingTimeTextView.text = context.resources.getString(
                    R.string.SavedTotalTimeTextView, totalTime
                )
            } else {
                binding.brewingTimeTextView.text = context.resources.getString(
                    R.string.SavedTotalTimeWithBloomTextView, item.brewTime, bloomTime
                )
            }

        }
    }

    fun createSwipeHelper(recyclerView: RecyclerView) {
        val swipeHelper: SwipeHelper = object : SwipeHelper(context, recyclerView) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                underlayButtons.add(
                    UnderlayButton(
                        "Delete",
                        0,
                        ContextCompat.getColor(context, R.color.red),
                        { pos -> // Delete selected item
                            showDeleteRecipeDialog(pos)
                        },
                        4
                    )
                )
                underlayButtons.add(
                    UnderlayButton(
                        "Brew",
                        0,
                        ContextCompat.getColor(context, R.color.green),
                        { pos ->
                            delegate.brewItem(pos)
                        },
                        4
                    )
                )
            }
        }
        swipeHelper.attachSwipe(context)
    }

    fun showDeleteRecipeDialog(position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.deleteRecipeDialogTitle)
            .setMessage(R.string.deleteRecipeDialogMessage)
            .setPositiveButton(R.string.delete) { _, _ ->
                delegate.deleteItem(currentList[position] as SavedRecipeDomain, position)
//                presenter!!.deleteRecipe(savedRecipes[position], context, position)
//                savedRecipes.removeAt(position)
//                notifyItemRemoved(position)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }
}

//
// class SavedRecipesRvAdapter(
//    private val savedRecipes: List<SavedRecipeDomain>,
//    private val context: Context,
//    private val recyclerView: RecyclerView
// ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private var presenter: SavedRecipesPresenter? = null
//    private val cardViewMarginAttr = 0
//    fun setPresenter(presenter: SavedRecipesPresenter?) {
//        this.presenter = presenter
//    }
//
//    class SavedRecipeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//        private val waterAndTempItem: TextView
//        private val beansWeightItem: TextView
//        private val beansGrindLevelItem: TextView
//        private val brewingMethodItem: TextView
//        private val timeItem: TextView
//        private val brewedOnItem: TextView
//
//        //        final private CardView cardView;
//        init {
//            waterAndTempItem = v.findViewById(R.id.waterAndTempTV)
//            beansWeightItem = v.findViewById(R.id.beansWeightTV)
//            beansGrindLevelItem = v.findViewById(R.id.beansGrindLevelTV)
//            brewingMethodItem = v.findViewById(R.id.brewMethodTV)
//            timeItem = v.findViewById(R.id.savedTimeTV)
//            brewedOnItem = v.findViewById(R.id.recipe_title)
//            //            this.cardView = v.findViewById(R.id.card_view);
//        }
//    }
//
//    override fun onCreateViewHolder(
//        viewGroup: ViewGroup,
//        i: Int
//    ): SavedRecipeViewHolder {
//        val v = LayoutInflater.from(viewGroup.context)
//            .inflate(R.layout.saved_recipe_item, viewGroup, false)
//
// //        CardView.LayoutParams lp = (CardView.LayoutParams) vh.cardView.getLayoutParams();
// //        cardViewMarginAttr = lp.bottomMargin;
//        return SavedRecipeViewHolder(v)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
//        val savedRecipeViewHolder = holder as SavedRecipeViewHolder
//        val total_water = savedRecipes[i].brewWaterAmount
//        val total_time = savedRecipes[i].bloomTime + savedRecipes[i].brewTime
//        val bloomTime = savedRecipes[i].bloomTime
//        val temp = savedRecipes[i].diceTemperature
//        val grindSize = savedRecipes[i].groundSize.substring(0, 1)
//            .uppercase(Locale.getDefault()) + savedRecipes[i].groundSize.substring(1).lowercase(
//            Locale.getDefault()
//        )
//        savedRecipeViewHolder.waterAndTempItem.text = context.resources.getString(
//            R.string.SavedWaterAndTempTextView,
//            total_water,
//            temp,
//            temp * 9 / 5 + 32
//        )
//        savedRecipeViewHolder.beansWeightItem.text = context.resources.getString(
//            R.string.SavedCoffeeWeightTextView,
//            savedRecipes[i].coffeeAmount
//        )
//        savedRecipeViewHolder.beansGrindLevelItem.text =
//            context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
//        savedRecipeViewHolder.brewingMethodItem.text = context.resources.getString(
//            R.string.SavedBrewingMethodTextView,
//            savedRecipes[i].brewingMethod
//        )
//        if (bloomTime == 0) savedRecipeViewHolder.timeItem.text =
//            context.resources.getString(
//                R.string.SavedTotalTimeTextView,
//                total_time
//            ) else savedRecipeViewHolder.timeItem.text = context.resources.getString(
//            R.string.SavedTotalTimeWithBloomTextView,
//            savedRecipes[i].brewTime,
//            bloomTime
//        )
//        savedRecipeViewHolder.brewedOnItem.text =
//            context.resources.getString(R.string.dateBrewedTextView, savedRecipes[i].dateBrewed)
//    }
//
//    override fun getItemCount(): Int {
//        return savedRecipes.size
//    }
//

//

// }
