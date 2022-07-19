package aeropresscipe.divinelink.aeropress.savedrecipes

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.RecipeCardItemBinding
import aeropresscipe.divinelink.aeropress.history.History
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.divinelink.core.util.extensions.toFahrenheit
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.swipe.SwipeMenuListener
import java.util.Locale

typealias OnActionClicked = (recipe: Any, action: SwipeAction) -> Unit

typealias OnLike = (recipe: Any, position: Int) -> Unit

class SavedRecipesAdapter(
    private val context: Context,
    private val onActionClicked: OnActionClicked,
    private val onLike: OnLike? = null
) : ListAdapter<Any, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) =
            if (oldItem is History && newItem is History) {
                oldItem.id == newItem.id
            } else {
                oldItem.hashCode() == newItem.hashCode()
            }

        override fun areContentsTheSame(oldItem: Any, newItem: Any) = compareItems(oldItem, newItem)

        private fun compareItems(oldItem: Any, newItem: Any): Boolean {
            if (oldItem is History && newItem is History) {
                return oldItem == newItem
            }
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
            if (oldItem is History && newItem is History) {
                return if (oldItem.isRecipeLiked == newItem.isRecipeLiked) {
                    super.getChangePayload(oldItem, newItem)
                } else {
                    newItem.isRecipeLiked
                }
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }
) {

    private val actionsBindHelper = ActionBindHelper()

    companion object {
        // fixme  Add EmptyList State that shows that list is empty.
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
        return when (viewType) {
            Type_Recipe -> {
                RecipeViewHolder(RecipeCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            Type_History -> {
                HistoryViewHolder(RecipeCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        val item = currentList[position]
        if (holder is HistoryViewHolder) {
            if (payloads.isEmpty()) {
                holder.updateView(item as History)
                actionsBindHelper.bind(item.id.toString(), holder.binding.savedRecipeItem)
                holder.binding.card.likeRecipeButton.setOnClickListener { onLike?.invoke(item, position) }
            } else {
                val like = payloads[0] as Boolean
                holder.update(like)
            }
        } else if (holder is RecipeViewHolder) {
            holder.updateView(item as SavedRecipeDomain)
            actionsBindHelper.bind(item.id.toString(), holder.binding.savedRecipeItem)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
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
            val grindSize = item.recipe.grindSize.size.substring(0, 1)
                .uppercase(Locale.getDefault()) + item.recipe.grindSize.size.substring(1).lowercase(
                Locale.getDefault()
            )
            binding.card.recipeTitle.text =
                context.resources.getString(R.string.dateBrewedTextView, item.dateBrewed)
            binding.card.waterAndTempTV.text = context.resources.getString(
                R.string.SavedWaterAndTempTextView,
                totalWater,
                temp,
                temp.toFahrenheit()
            )
            binding.card.beansWeightTV.text =
                context.resources.getString(R.string.SavedCoffeeWeightTextView, item.recipe.coffeeAmount)
            binding.card.beansGrindLevelTV.text =
                context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
            binding.card.brewingMethodTextView.text =
                context.resources.getString(R.string.SavedBrewingMethodTextView, item.recipe.brewMethod.method)

            if (bloomTime == 0L) {
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
            // Intentionally Empty.
        }

        override fun onOpened(view: View) {
            val recipe = currentList[layoutPosition] as SavedRecipeDomain
            actionsBindHelper.closeOtherThan(recipe.id.toString())
        }

        override fun onFullyOpened(view: View, quickAction: SwipeAction) {
            // Intentionally Empty.
        }

        override fun onActionClicked(view: View, action: SwipeAction) {
            onActionClicked(currentList[layoutPosition] as SavedRecipeDomain, action)
        }
    }

    inner class HistoryViewHolder(val binding: RecipeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root), SwipeMenuListener {

        private val swipeToAction = binding.savedRecipeItem

        @SuppressLint("ClickableViewAccessibility")
        fun updateView(item: History) {
            swipeToAction.menuListener = this
            val totalWater = item.recipe.brewWaterAmount
            val totalTime = item.recipe.bloomTime + item.recipe.brewTime
            val bloomTime = item.recipe.bloomTime
            val temp = item.recipe.diceTemperature
            val grindSize = item.recipe.grindSize.size.substring(0, 1)
                .uppercase(Locale.getDefault()) + item.recipe.grindSize.size.substring(1).lowercase(
                Locale.getDefault()
            )
            binding.card.recipeTitle.text =
                context.resources.getString(R.string.dateBrewedTextView, item.dateBrewed)
            binding.card.waterAndTempTV.text = context.resources.getString(
                R.string.SavedWaterAndTempTextView,
                totalWater,
                temp,
                temp.toFahrenheit()
            )
            binding.card.beansWeightTV.text =
                context.resources.getString(R.string.SavedCoffeeWeightTextView, item.recipe.coffeeAmount)
            binding.card.beansGrindLevelTV.text =
                context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
            binding.card.brewingMethodTextView.text =
                context.resources.getString(R.string.SavedBrewingMethodTextView, item.recipe.brewMethod.method)

            if (bloomTime == 0L) {
                binding.card.brewingTimeTextView.text = context.resources.getString(
                    R.string.SavedTotalTimeTextView, totalTime
                )
            } else {
                binding.card.brewingTimeTextView.text = context.resources.getString(
                    R.string.SavedTotalTimeWithBloomTextView, item.recipe.brewTime, bloomTime
                )
            }

            binding.card.likeRecipeLayout.visibility = View.VISIBLE
            update(item.isRecipeLiked)

            binding.card.likeRecipeButton.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    val reducer = AnimatorInflater.loadAnimator(context, R.animator.reduce_size) as AnimatorSet
                    reducer.setTarget(view)
                    reducer.start()
                } else if (motionEvent.action == MotionEvent.ACTION_UP) {
                    val regainer = AnimatorInflater.loadAnimator(context, R.animator.regain_size) as AnimatorSet
                    regainer.setTarget(view)
                    regainer.start()
                }
                false
            }
        }

        fun update(like: Boolean) {
            when (like) {
                true -> binding.card.likeRecipeButton.setImageResource(R.drawable.ic_heart_on)
                false -> binding.card.likeRecipeButton.setImageResource(R.drawable.ic_heart_off)
            }
        }

        override fun onClosed(view: View) {
            // Intentionally Empty.
        }

        override fun onOpened(view: View) {
            val recipe = currentList[layoutPosition] as History
            actionsBindHelper.closeOtherThan(recipe.id.toString())
        }

        override fun onFullyOpened(view: View, action: SwipeAction) {
//            onActionClicked(currentList[layoutPosition] as History, action)
        }

        override fun onActionClicked(view: View, action: SwipeAction) {
            onActionClicked(currentList[layoutPosition] as History, action)
        }
    }
}
