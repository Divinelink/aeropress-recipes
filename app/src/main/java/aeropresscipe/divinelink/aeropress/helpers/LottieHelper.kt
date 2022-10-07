package aeropresscipe.divinelink.aeropress.helpers

import aeropresscipe.divinelink.aeropress.R
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.model.KeyPath
import gr.divinelink.core.util.extensions.changeLayersColor

object LottieHelper {
    fun updateLikeButton(likeButton: LottieAnimationView?) {
        likeButton?.changeLayersColor(R.color.colorPrimary, KeyPath("Heart Fill", "**"))
        likeButton?.changeLayersColor(R.color.colorPrimary, KeyPath("Circle 2", "**"))
        likeButton?.changeLayersColor(R.color.colorPrimary, KeyPath("Circle 1", "**"))
        likeButton?.changeLayersColor(R.color.colorPrimary, KeyPath("Heart Fill Small 4", "**"))
        likeButton?.changeLayersColor(R.color.colorOnBackground, KeyPath("Heart Stroke 2", "**"))
        likeButton?.changeLayersColor(R.color.colorOnBackground, KeyPath("Heart Stroke", "**"))
        likeButton?.changeLayersColor(R.color.colorOnPrimaryContainer, KeyPath("Heart Fill Small 2", "**"))
        likeButton?.changeLayersColor(R.color.colorOnPrimaryContainer, KeyPath("Heart Fill Small 3", "**"))
    }
}
