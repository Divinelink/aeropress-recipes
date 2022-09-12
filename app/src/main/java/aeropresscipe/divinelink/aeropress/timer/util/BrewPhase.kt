package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

class BrewPhase private constructor(
    var brewStates: MutableList<BrewState>?,
) {
    data class Builder(
        var brewStates: MutableList<BrewState>? = null,
        var currentState: BrewState? = null,
    ) {
        fun brewStates(brewStates: MutableList<BrewState>?) = apply { this.brewStates = brewStates }
        fun build() = BrewPhase(
            brewStates = brewStates,
        )
    }

    fun getCurrentState(): BrewState {
        return brewStates?.firstOrNull() ?: BrewState.Finished
    }

    fun removeCurrentPhase() {
        brewStates?.removeFirstOrNull()
    }
}

sealed class BrewState(
    @StringRes open val title: Int,
    @StringRes open val description: Int,
    open val brewWater: Int,
    open val brewTime: Long,
    open val phase: Phase,
    open val update: Boolean,
    open val animate: Boolean
) {
    data class Bloom(val water: Int, val time: Long) : BrewState(
        title = R.string.bloomPhase,
        description = R.string.bloomPhaseWaterText,
        water,
        time,
        Phase.Bloom,
        true,
        false)

    data class Brew(val water: Int, val time: Long) : BrewState(
        title = R.string.brewPhase,
        description = R.string.brewPhaseNoBloom,
        water, time,
        Phase.Brew,
        true,
        true
    )

    data class BrewWithBloom(val water: Int, val time: Long) : BrewState(
        title = R.string.brewPhase,
        description = R.string.brewPhaseWithBloom,
        water,
        time,
        Phase.Brew,
        true,
        true
    )

    object Finished : BrewState(
        title = R.string.finishPhaseTitle,
        description = R.string.finishPhaseDescription,
        brewWater = 0,
        brewTime = 0,
        Phase.Finish,
        false,
        true
    )
}

enum class Phase {
    Bloom,
    Brew,
    Finish
}
