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
