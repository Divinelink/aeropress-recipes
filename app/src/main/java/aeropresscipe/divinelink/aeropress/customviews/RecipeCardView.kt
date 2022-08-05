package aeropresscipe.divinelink.aeropress.customviews

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.databinding.ViewRecipeCardBinding
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import gr.divinelink.core.util.extensions.toFahrenheit
import java.util.Locale

class RecipeCardView : FrameLayout {
    var binding: ViewRecipeCardBinding = ViewRecipeCardBinding.inflate(LayoutInflater.from(context), this, false)

    private var likeButtonListener: LikeButtonListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addView(binding.root)
    }

    fun enableLikeButton(enable: Boolean) {
        if (enable) {
            binding.likeRecipeLayout.visibility = View.VISIBLE
        } else {
            binding.likeRecipeLayout.visibility = View.GONE
        }
    }

    fun setRecipe(card: RecipeCard) {
        val totalWater = card.recipe.brewWaterAmount
        val totalTime = card.recipe.bloomTime + card.recipe.brewTime
        val bloomTime = card.recipe.bloomTime
        val temp = card.recipe.diceTemperature
        val grindSize = card.recipe.grindSize.size
            .substring(0, 1)
            .uppercase(Locale.getDefault()) + card.recipe.grindSize.size.substring(1)
            .lowercase(Locale.getDefault())

        binding.recipeTitle.visibility = card.brewDateVisibility
        binding.recipeTitle.text = context.resources.getString(R.string.dateBrewedTextView, card.brewDate)

        binding.waterAndTempTV.text = context.resources.getString(R.string.SavedWaterAndTempTextView, totalWater, temp, temp.toFahrenheit())
        binding.beansWeightTV.text = context.resources.getString(R.string.SavedCoffeeWeightTextView, card.recipe.coffeeAmount)
        binding.beansGrindLevelTV.text = context.resources.getString(R.string.SavedGrindLevelTextView, grindSize)
        binding.brewingMethodTextView.text = context.resources.getString(R.string.SavedBrewingMethodTextView, card.recipe.brewMethod.method)

        if (bloomTime == 0L) {
            binding.brewingTimeTextView.text = context.resources.getString(
                R.string.SavedTotalTimeTextView, totalTime
            )
        } else {
            binding.brewingTimeTextView.text = context.resources.getString(
                R.string.SavedTotalTimeWithBloomTextView, card.recipe.brewTime, bloomTime
            )
        }

        binding.likeRecipeLayout.visibility = card.likeButtonVisibility
    }


    fun setOnLikeButtonListener(l: LikeButtonListener?) {
        this.likeButtonListener = l
    }

    fun setOnLikeButtonListener(l: () -> Unit) {
        this.likeButtonListener = LikeButtonListener {
            l.invoke()
        }
    }

    fun interface LikeButtonListener {
        fun onClickListener()
    }

}
