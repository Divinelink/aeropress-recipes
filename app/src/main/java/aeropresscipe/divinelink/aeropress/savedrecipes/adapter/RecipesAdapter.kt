package aeropresscipe.divinelink.aeropress.savedrecipes.adapter

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.RecipeCard
import aeropresscipe.divinelink.aeropress.databinding.EmptyRecyclerLayoutBinding
import aeropresscipe.divinelink.aeropress.databinding.ViewSwipeRecipeCardBinding
import aeropresscipe.divinelink.aeropress.history.History
import aeropresscipe.divinelink.aeropress.savedrecipes.SavedRecipeDomain
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

typealias OnActionClicked = (recipe: Any, action: SwipeAction) -> Unit

class RecipesAdapter(
    private val context: Context,
    private val onActionClicked: OnActionClicked,
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

    object EmptyFavorites

    companion object {
        const val Recipe = 0
        const val Empty_Favorites = 2
        const val Empty_History = 4
        const val Type_Error = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SavedRecipeDomain -> {
                Recipe
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
            Recipe -> RecipeViewHolder(ViewSwipeRecipeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
            is RecipeViewHolder -> {
                holder.updateView(item as SavedRecipeDomain)
                actionsBindHelper.bind(item.id.toString(), holder.binding.swipeActionLayout)
            }
            is EmptyViewHolder -> {
                holder.update()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    inner class RecipeViewHolder(var binding: ViewSwipeRecipeCardBinding) :
        RecyclerView.ViewHolder(binding.root), SwipeMenuListener {
        private val swipeToAction = binding.swipeActionLayout

        fun updateView(item: SavedRecipeDomain) {
            swipeToAction.menuListener = this
            swipeToAction.setActionsRes(R.menu.favorites_action_menu)
            binding.card.setRecipe(RecipeCard.FavoritesCard(item.recipe, item.dateBrewed))
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

}
