package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.timer.util.BrewState
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
) : Serializable {

    @Ignore
    var withBloom: Boolean = bloomTime != 0

    @Ignore
    var remainingWater: Int = brewWaterAmount - bloomWater

    fun getBrewStates(): MutableList<BrewState> {
        val brewStates: MutableList<BrewState> = mutableListOf()
        if (withBloom) {
            brewStates.add(BrewState.Bloom(water = bloomWater, time = bloomTime.toLong()))
            brewStates.add(BrewState.BrewWithBloom(water = remainingWater, time = brewTime.toLong()))
        } else {
            brewStates.add(BrewState.Brew(water = brewWaterAmount, time = brewTime.toLong()))
        }
        brewStates.add(BrewState.Finished)
        return brewStates
    }
}
