package aeropresscipe.divinelink.aeropress.generaterecipe

import aeropresscipe.divinelink.aeropress.timer.util.Phase
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


    fun getCurrentPhase(): Phase {
        return if (bloomTime == 0) {
            Phase.BREW
        } else {
            Phase.BLOOM
        }
    }

    fun getPhases(): MutableList<Phase> {
        val phases: MutableList<Phase> = mutableListOf()
        if (withBloom) {
            phases.add(Phase.BLOOM)
        }
        phases.add(Phase.BREW)
        phases.add(Phase.FINISHED)
        return phases
    }
}
