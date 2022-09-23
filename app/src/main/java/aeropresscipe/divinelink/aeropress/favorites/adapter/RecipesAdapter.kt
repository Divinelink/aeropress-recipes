package aeropresscipe.divinelink.aeropress.favorites.adapter

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.recipecard.RecipeCard
import aeropresscipe.divinelink.aeropress.databinding.EmptyRecyclerLayoutBinding
import aeropresscipe.divinelink.aeropress.databinding.ViewSwipeRecipeCardBinding
import aeropresscipe.divinelink.aeropress.favorites.Favorites
import aeropresscipe.divinelink.aeropress.history.HistoryItem
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder
import android.view.View
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.swipe.SwipeMenuListener

typealias OnActionClicked = (recipe: Favorites, action: SwipeAction) -> Unit

object FavoriteItem {
    fun register(
        mappingAdapter: MappingAdapter,
        onActionClicked: OnActionClicked,
        actionBindHelper: ActionBindHelper,
    ) {
        mappingAdapter.registerFactory(
            LayoutFactory({ layoutInflater, root ->
                RecipeViewHolder(
                    binding = ViewSwipeRecipeCardBinding.inflate(layoutInflater, root, false),
                    actionsBindHelper = actionBindHelper,
                    onActionClicked = onActionClicked,
                )
            })
        )
        mappingAdapter.registerFactory<EmptyType.EmptyFavorites>(
            LayoutFactory({ layoutInflater, root ->
                HistoryItem.EmptyViewHolder(EmptyRecyclerLayoutBinding.inflate(layoutInflater, root, false))
            })
        )
    }

    class RecipeViewHolder(
        private val binding: ViewSwipeRecipeCardBinding,
        private val actionsBindHelper: ActionBindHelper,
        private val onActionClicked: OnActionClicked,
    ) :
        MappingViewHolder<Favorites>(binding.root), SwipeMenuListener {
        private val swipeToAction = binding.swipeActionLayout
        private lateinit var model: Favorites

        override fun bind(model: Favorites) {
            this.model = model
            actionsBindHelper.bind(model.id.toString(), binding.swipeActionLayout)
            swipeToAction.menuListener = this
            swipeToAction.setActionsRes(R.menu.favorites_action_menu)
            binding.card.setRecipe(RecipeCard.FavoritesCard(model.recipe, model.dateBrewed))
        }

        override fun onClosed(view: View) {
            // Intentionally Empty.
        }

        override fun onOpened(view: View) {
            actionsBindHelper.closeOtherThan(model.id.toString())
        }

        override fun onFullyOpened(view: View, quickAction: SwipeAction) {
            // Intentionally Empty.
        }

        override fun onActionClicked(view: View, action: SwipeAction) {
            onActionClicked(model, action)
            actionsBindHelper.closeAll()
        }
    }
}
