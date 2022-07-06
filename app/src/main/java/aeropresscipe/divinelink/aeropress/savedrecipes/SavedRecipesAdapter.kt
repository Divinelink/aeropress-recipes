package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.RecipeCardItemBinding
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.swipe.SwipeMenuListener
import java.util.Locale

typealias OnActionClicked = (recipe: SavedRecipeDomain, action: SwipeAction) -> Unit

class SavedRecipesAdapter(
    private val context: Context,
    private val onActionClicked: OnActionClicked
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

    private val actionsBindHelper = ActionBindHelper()

    companion object {
        // TODO: Add EmptyList State that shows that list is empty.
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
            actionsBindHelper.bind(item.id.toString(), holder.binding.savedRecipeItem)
        }

        holder.itemView.setOnClickListener {
//            delegate.onItemClick(item = item)
        }
    }

    inner class RecipeViewHolder(var binding: RecipeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root), SwipeMenuListener {

        private val swipeToAction = binding.savedRecipeItem

        fun updateView(item: SavedRecipeDomain) {
            swipeToAction.menuListener = this
            val totalWater = item.recipe.brewWaterAmount
            val totalTime = item.recipe.bloomTime + item.recipe.brewTime
            val bloomTime = item.recipe.bloomTime
            val temp = item.recipe.diceTemperature
            val grindSize = item.recipe.groundSize.substring(0, 1)
                .uppercase(Locale.getDefault()) + item.recipe.groundSize.substring(1).lowercase(
                Locale.getDefault()
            )
            binding.card.recipeTitle.text =
                context.resources.getString(R.string.dateBrewedTextView, item.dateBrewed)
            binding.card.waterAndTempTV.text = context.resources.getString(
                R.string.SavedWaterAndTempTextView,
                totalWater,
                temp,
                temp * 9 / 5 + 32
            )
            binding.card.beansWeightTV.text =
                context.resources.getString(R.string.SavedCoffeeWeightTextView, item.recipe.coffeeAmount)
            binding.card.beansGrindLevelTV.text =
                context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
            binding.card.brewingMethodTextView.text =
                context.resources.getString(R.string.SavedBrewingMethodTextView, item.recipe.brewingMethod)

            if (bloomTime == 0) {
                binding.card.brewingTimeTextView.text = context.resources.getString(
                    R.string.SavedTotalTimeTextView, totalTime
                )
            } else {
                binding.card.brewingTimeTextView.text = context.resources.getString(
                    R.string.SavedTotalTimeWithBloomTextView, item.recipe.brewTime, bloomTime
                )
            }
        }

        override fun onClosed(view: View) {
            // empty
        }

        override fun onOpened(view: View) {
            val recipe = currentList[layoutPosition] as SavedRecipeDomain
            actionsBindHelper.closeOtherThan(recipe.id.toString())
        }

        override fun onFullyOpened(view: View, quickAction: SwipeAction) {
            // empty
        }

        override fun onActionClicked(view: View, action: SwipeAction) {
            onActionClicked(currentList[layoutPosition] as SavedRecipeDomain, action)
        }
    }
}
