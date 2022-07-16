package aeropresscipe.divinelink.aeropress.generaterecipe.models

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.generaterecipe.CoffeeGrindSize
import androidx.annotation.StringRes

sealed class RecipeStep(
    @StringRes open val stepText: Int,
) {
    data class HeatWaterStep(val waterAmount: Int, val temperature: Int) : RecipeStep(stepText = R.string.heatWaterText)
    data class CoffeeGrindStep(val coffeeAmount: Int, val grindSize: CoffeeGrindSize) : RecipeStep(R.string.grindCoffeeText)
    object StandardMethodStep : RecipeStep(R.string.normal_orientation_text)
    object InvertedMethodStep : RecipeStep(R.string.inverted_orientation_text)
    object PourGroundCoffeeStep : RecipeStep(R.string.pourInCoffee)

    // Bloom Region
    data class BloomStep(val bloomWater: Int, val bloomTime: Int) : RecipeStep(R.string.addWaterText)
    data class RemainingWaterStep(val remainingWater: Int) : RecipeStep(R.string.addRemainingWater)

    // End Bloom Region
    // -- -- -- -- -- --
    // No Bloom Region
    data class PourWaterStep(val waterAmount: Int) : RecipeStep(R.string.addWaterSlowly)

    // End No Bloom Region
    data class WaitToBrewStep(val minutes: Long, val seconds: Long) : RecipeStep(R.string.waitToBrewText)

    object FlipToNormalOrientation : RecipeStep(R.string.upsideDownMethodText)

    object PressStep : RecipeStep(R.string.pressText)
}
