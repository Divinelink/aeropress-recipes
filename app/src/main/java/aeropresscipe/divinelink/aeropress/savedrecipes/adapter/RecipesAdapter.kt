package aeropresscipe.divinelink.aeropress.savedrecipes.adapter

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.EmptyRecyclerLayoutBinding
import aeropresscipe.divinelink.aeropress.databinding.RecipeCardItemBinding
import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
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

class RecipesAdapter(
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

    object EmptyHistory
    object EmptyFavorites

    companion object {
        const val Recipe = 0
        const val Empty_Favorites = 2
        const val History = 3
        const val Empty_History = 4
        const val Type_Error = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SavedRecipeDomain -> {
                Recipe
            }
            is History -> {
                History
            }
            is EmptyHistory -> {
                Empty_History
            }
            is EmptyFavorites -> {
                Empty_Favorites
            }
            else -> Type_Error
        }
    }

    override fun submitList(list: List<Any>?) {
        super.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Recipe -> RecipeViewHolder(RecipeCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            History -> HistoryViewHolder(RecipeCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            Empty_History -> {
                EmptyViewHolder(
                    EmptyRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    EmptyType.EmptyHistory
                )
            }
            Empty_Favorites -> {
                EmptyViewHolder(
                    EmptyRecyclerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                    EmptyType.EmptyFavorites
                )
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
        when (holder) {
            is HistoryViewHolder -> {
                if (payloads.isEmpty()) {
                    holder.updateView(item as History)
                    actionsBindHelper.bind(item.id.toString(), holder.binding.savedRecipeItem)
                    holder.binding.card.likeRecipeButton.setOnClickListener { onLike?.invoke(item, position) }
                } else {
                    val like = payloads[0] as Boolean
                    holder.update(like)
                }
            }
            is RecipeViewHolder -> {
                holder.updateView(item as SavedRecipeDomain)
                actionsBindHelper.bind(item.id.toString(), holder.binding.savedRecipeItem)
            }
            is EmptyViewHolder -> {
                holder.update()
            }
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
            setCard(binding, item.recipe, item.dateBrewed)
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

    inner class EmptyViewHolder(
        private val binding: EmptyRecyclerLayoutBinding,
        private val type: EmptyType
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun update() {
            binding.root.text = context.resources.getString(type.text)
            binding.root.setCompoundDrawablesWithIntrinsicBounds(type.image, 0, 0, 0)
        }
    }

    inner class HistoryViewHolder(val binding: RecipeCardItemBinding) :
        RecyclerView.ViewHolder(binding.root), SwipeMenuListener {
        private val swipeToAction = binding.savedRecipeItem

        @SuppressLint("ClickableViewAccessibility")
        fun updateView(item: History) {
            setCard(binding, item.recipe, item.dateBrewed)
            swipeToAction.menuListener = this

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
            onActionClicked(currentList[layoutPosition] as History, action)
        }

        override fun onActionClicked(view: View, action: SwipeAction) {
            onActionClicked(currentList[layoutPosition] as History, action)
        }
    }

    private fun setCard(binding: RecipeCardItemBinding, recipe: Recipe, brewDate: String) {
        val totalWater = recipe.brewWaterAmount
        val totalTime = recipe.bloomTime + recipe.brewTime
        val bloomTime = recipe.bloomTime
        val temp = recipe.diceTemperature
        val grindSize = recipe.grindSize.size.substring(0, 1)
            .uppercase(Locale.getDefault()) + recipe.grindSize.size.substring(1).lowercase(
            Locale.getDefault()
        )
        binding.card.recipeTitle.text =
            context.resources.getString(R.string.dateBrewedTextView, brewDate)
        binding.card.waterAndTempTV.text = context.resources.getString(
            R.string.SavedWaterAndTempTextView, totalWater, temp, temp.toFahrenheit()
        )
        binding.card.beansWeightTV.text =
            context.resources.getString(R.string.SavedCoffeeWeightTextView, recipe.coffeeAmount)
        binding.card.beansGrindLevelTV.text =
            context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
        binding.card.brewingMethodTextView.text =
            context.resources.getString(R.string.SavedBrewingMethodTextView, recipe.brewMethod.method)

        if (bloomTime == 0L) {
            binding.card.brewingTimeTextView.text = context.resources.getString(
                R.string.SavedTotalTimeTextView, totalTime
            )
        } else {
            binding.card.brewingTimeTextView.text = context.resources.getString(
                R.string.SavedTotalTimeWithBloomTextView, recipe.brewTime, bloomTime
            )
        }
    }
}
