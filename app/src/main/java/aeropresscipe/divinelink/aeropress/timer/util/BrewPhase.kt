package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

class BrewPhase private constructor(
    var brewStates: MutableList<BrewState>,
    val brewState: BrewState,
) {
    data class Builder(
        var brewStates: MutableList<BrewState>? = null,
        var brewState: BrewState? = null,
    ) {
        fun brewStates(brewStates: MutableList<BrewState>?) = apply { this.brewStates = brewStates }
        fun brewState(brewState: BrewState?) = apply { this.brewState = brewState }
        fun build() = BrewPhase(
            brewStates = brewStates!!,
            brewState = brewState!!,
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
) {
    data class Bloom(val water: Int, val time: Long) : BrewState(title = R.string.bloomPhase, description = R.string.bloomPhaseWaterText, water, time)
    data class Brew(val water: Int, val time: Long) : BrewState(title = R.string.brewPhase, description = R.string.brewPhaseNoBloom, water, time)
    data class BrewWithBloom(val water: Int, val time: Long) : BrewState(title = R.string.brewPhase, description = R.string.brewPhaseWithBloom, water, time)
    object Finished : BrewState(title = 0, description = 0, brewWater = 0, brewTime = 0)
}