package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

class BrewPhase private constructor(
    var brewStates: MutableList<BrewState>,
) {
    data class Builder(
        var brewStates: MutableList<BrewState>? = null,
        var currentState: BrewState? = null,
    ) {
        fun brewStates(brewStates: MutableList<BrewState>?) = apply { this.brewStates = brewStates }
        fun build() = BrewPhase(
            brewStates = brewStates!!,
        )
    }

    fun getCurrentState(): BrewState {
        return brewStates.firstOrNull() ?: BrewState.Finished
    }

    fun removeCurrentPhase() {
        brewStates.removeFirstOrNull()
    }
}

sealed class BrewState(
    @StringRes open val title: Int,
    @StringRes open val description: Int,
    open val brewWater: Int,
    open val brewTime: Long,
    open val phase: Phase
) {
    data class Bloom(val water: Int, val time: Long) : BrewState(
        title = R.string.bloomPhase,
        description = R.string.bloomPhaseWaterText,
        water,
        time,
        Phase.Bloom)

    data class Brew(val water: Int, val time: Long) : BrewState(
        title = R.string.brewPhase,
        description = R.string.brewPhaseNoBloom,
        water, time,
        Phase.Brew
    )

    data class BrewWithBloom(val water: Int, val time: Long) : BrewState(
        title = R.string.brewPhase,
        description = R.string.brewPhaseWithBloom,
        water,
        time,
        Phase.Brew
    )

    object Finished : BrewState(title = 0, description = 0, brewWater = 0, brewTime = 0, Phase.Finish)
}

enum class Phase {
    Bloom,
    Brew,
    Finish
}
