package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.RecipeCard
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardView
import aeropresscipe.divinelink.aeropress.databinding.EmptyRecyclerLayoutBinding
import aeropresscipe.divinelink.aeropress.databinding.ViewSwipeRecipeCardBinding
import aeropresscipe.divinelink.aeropress.helpers.LottieHelper
import aeropresscipe.divinelink.aeropress.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.mapping.MappingViewHolder
import aeropresscipe.divinelink.aeropress.savedrecipes.adapter.EmptyType
import android.view.View
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.swipe.SwipeMenuListener

typealias OnActionClicked = (recipe: History, action: SwipeAction) -> Unit
typealias OnLike = (recipe: History, position: Int) -> Unit

object HistoryItem {

    @Suppress("unchecked_cast")
    fun register(
        mappingAdapter: MappingAdapter,
        onActionClicked: OnActionClicked,
        actionBindHelper: ActionBindHelper,
        onLike: OnLike
    ) {
        mappingAdapter.registerFactory(
            History::class.java,
            LayoutFactory(
                viewHolder = { layoutInflater, root ->
                    HistoryMappingViewHolder(
                        binding = ViewSwipeRecipeCardBinding.inflate(layoutInflater, root, false),
                        onActionClicked = onActionClicked,
                        onLike = onLike,
                        actionsBindHelper = actionBindHelper
                    )
                },
            )
        )
        mappingAdapter.registerFactory(
            EmptyType.EmptyHistory::class.java as Class<EmptyType>,
            factory = LayoutFactory(
                viewHolder = { layoutInflater, root ->
                    EmptyViewHolder(
                        EmptyRecyclerLayoutBinding.inflate(layoutInflater, root, false),
                    )
                }
            )
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
        }
    }

    class EmptyViewHolder(
        private val binding: EmptyRecyclerLayoutBinding
    ) :
        MappingViewHolder<EmptyType>(binding.root) {

        override fun bind(model: EmptyType) {
            binding.root.text = context.resources.getString(model.text)
            binding.root.setCompoundDrawablesWithIntrinsicBounds(model.image, 0, 0, 0)
        }
    }

}
