package aeropresscipe.divinelink.aeropress.history

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.customviews.RecipeCard
import aeropresscipe.divinelink.aeropress.customviews.RecipeCardView
import aeropresscipe.divinelink.aeropress.customviews.SaveRecipeCardView
import aeropresscipe.divinelink.aeropress.helpers.LottieHelper
import aeropresscipe.divinelink.aeropress.mapping.LayoutFactory
import aeropresscipe.divinelink.aeropress.mapping.MappingAdapter
import aeropresscipe.divinelink.aeropress.mapping.MappingViewHolder
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import gr.divinelink.core.util.swipe.ActionBindHelper
import gr.divinelink.core.util.swipe.SwipeAction
import gr.divinelink.core.util.swipe.SwipeMenuListener
import gr.divinelink.core.util.swipe.SwipeToActionLayout

typealias OnActionClicked = (recipe: History, action: SwipeAction) -> Unit
typealias OnLike = (recipe: History, position: Int) -> Unit

object HistoryItem {

    fun register(
        mappingAdapter: MappingAdapter,
        onActionClicked: OnActionClicked,
        actionBindHelper: ActionBindHelper,
        onLike: OnLike
    ) {
        mappingAdapter.registerFactory(
            History::class.java,
            LayoutFactory({ HistoryMappingViewHolder(it, actionBindHelper, onActionClicked, onLike) }, R.layout.view_swipe_recipe_card)
        )
    }


    class HistoryMappingViewHolder(
        itemView: View,
        private val actionsBindHelper: ActionBindHelper,
        private val onActionClicked: OnActionClicked,
        private val onLike: OnLike,
    ) :
        MappingViewHolder<History>(itemView),
        SwipeMenuListener {
        private val card = findViewById<RecipeCardView>(R.id.card)
        private val likeButton = findViewById<LottieAnimationView>(R.id.likeButton)
        private val swipeToAction = findViewById<SwipeToActionLayout>(R.id.swipe_action_layout)

        private lateinit var model: History

        override fun bind(model: History) {
            this.model = model
            if (payload.isEmpty()) {
                actionsBindHelper.bind(model.id.toString(), swipeToAction)
                card.setRecipe(RecipeCard.HistoryCard(model.recipe, model.dateBrewed))
                swipeToAction.setActionsRes(R.menu.history_action_menu)
                swipeToAction.menuListener = this

                LottieHelper.updateLikeButton(likeButton)
                update(model.isRecipeLiked)
                card.setOnLikeButtonListener { onLike.invoke(model, layoutPosition) }
            } else {
                updateWithAnimation(payload[0] as Boolean)
            }
        }


        private fun update(like: Boolean) {
            when (like) {
                true -> likeButton.frame = SaveRecipeCardView.LIKE_MAX_FRAME
                false -> likeButton.frame = SaveRecipeCardView.DISLIKE_MAX_FRAME
            }
        }

        private fun updateWithAnimation(like: Boolean) {
            when (like) {
                true -> likeButton.setMinAndMaxFrame(SaveRecipeCardView.LIKE_MIN_FRAME, SaveRecipeCardView.LIKE_MAX_FRAME)
                false -> likeButton.setMinAndMaxFrame(SaveRecipeCardView.DISLIKE_MIN_FRAME, SaveRecipeCardView.DISLIKE_MAX_FRAME)
            }
            likeButton.clipToCompositionBounds = false
            likeButton.playAnimation()
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

}
