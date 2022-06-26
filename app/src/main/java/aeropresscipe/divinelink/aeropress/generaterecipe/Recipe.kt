package aeropresscipe.divinelink.aeropress.generaterecipe

import androidx.room.Ignore
import java.io.Serializable

data class Recipe(
    var diceTemperature: Int,
    var brewTime: Int,
    var bloomTime: Int,
    var bloomWater: Int,
    var coffeeAmount: Int,
    var brewWaterAmount: Int,
    var groundSize: String,
    var brewingMethod: String,
    @Ignore var isNewRecipe: Boolean = false,
    @Ignore var withBloom: Boolean = bloomTime != 0,
    @Ignore var remainingWater: Int = brewWaterAmount - bloomWater,
) : Serializable
