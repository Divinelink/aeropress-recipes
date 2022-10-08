package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.components.recipecard.RecipeCard
import aeropresscipe.divinelink.aeropress.components.saverecipecard.SaveRecipeCardView
import aeropresscipe.divinelink.aeropress.databinding.EmptyRecyclerLayoutBinding
import aeropresscipe.divinelink.aeropress.databinding.ViewSwipeRecipeCardBinding
import aeropresscipe.divinelink.aeropress.favorites.adapter.EmptyType
import aeropresscipe.divinelink.aeropress.helpers.LottieHelper
import aeropresscipe.divinelink.aeropress.util.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.util.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.util.mapping.MappingViewHolder
import android.view.View
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.swipe.SwipeMenuListener

typealias OnActionClicked = (recipe: History, action: SwipeAction) -> Unit
typealias OnLike = (recipe: History, position: Int) -> Unit

class HistoryAdapter(
    val onActionClicked: OnActionClicked,
    val actionBindHelper: ActionBindHelper,
    val onLike: OnLike,
) : MappingAdapter() {

    override fun register() {
        registerFactory(
            LayoutFactory({ layoutInflater, root ->
                HistoryMappingViewHolder(
                    binding = ViewSwipeRecipeCardBinding.inflate(layoutInflater, root, false),
                    actionsBindHelper = actionBindHelper,
                    onActionClicked = onActionClicked,
                    onLike = onLike
                )
            })
        )
        registerFactory<EmptyType.EmptyHistory>(
            LayoutFactory({ layoutInflater, root ->
                EmptyViewHolder(EmptyRecyclerLayoutBinding.inflate(layoutInflater, root, false))
            })
        )
    }

    class HistoryMappingViewHolder(
        private val binding: ViewSwipeRecipeCardBinding,
        private val actionsBindHelper: ActionBindHelper,
        private val onActionClicked: OnActionClicked,
        private val onLike: OnLike,
    ) :
        MappingViewHolder<History>(binding.root),
        SwipeMenuListener {

        private lateinit var model: History

        override fun bind(model: History) {
            this.model = model
            if (payload.isEmpty()) {
                actionsBindHelper.bind(model.id.toString(), binding.swipeActionLayout)
                binding.card.setRecipe(RecipeCard.HistoryCard(model.recipe, model.dateBrewed))
                binding.swipeActionLayout.setActionsRes(R.menu.history_action_menu)
                binding.swipeActionLayout.menuListener = this

                LottieHelper.updateLikeButton(binding.card.binding.likeButton)
                update(model.isRecipeLiked)
                binding.card.setOnLikeButtonListener { onLike.invoke(model, layoutPosition) }
            } else {
                updateWithAnimation(payload[0] as Boolean)
            }
        }

        private fun update(like: Boolean) {
            when (like) {
                true -> binding.card.binding.likeButton.frame = SaveRecipeCardView.LIKE_MAX_FRAME
                false -> binding.card.binding.likeButton.frame = SaveRecipeCardView.DISLIKE_MAX_FRAME
            }
        }

        private fun updateWithAnimation(like: Boolean) {
            when (like) {
                true -> binding.card.binding.likeButton.setMinAndMaxFrame(SaveRecipeCardView.LIKE_MIN_FRAME, SaveRecipeCardView.LIKE_MAX_FRAME)
                false -> binding.card.binding.likeButton.setMinAndMaxFrame(SaveRecipeCardView.DISLIKE_MIN_FRAME, SaveRecipeCardView.DISLIKE_MAX_FRAME)
            }
            binding.card.binding.likeButton.clipToCompositionBounds = false
            binding.card.binding.likeButton.playAnimation()
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

    companion object {
        class EmptyViewHolder<T : EmptyType<T>>(
            private val binding: EmptyRecyclerLayoutBinding,
        ) :
            MappingViewHolder<T>(binding.root) {

            override fun bind(model: T) {
                binding.root.text = context.resources.getString(model.text)
                binding.root.setCompoundDrawablesWithIntrinsicBounds(model.image, 0, 0, 0)
            }
        }
    }
}
