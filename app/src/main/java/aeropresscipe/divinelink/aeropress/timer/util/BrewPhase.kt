package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.R
import aeropresscipe.divinelink.aeropress.timer.Phase
import androidx.annotation.StringRes


class BrewPhase private constructor(
//    var phases: MutableList<Phase>,
    var currentPhase: Phase,
    var brewTime: Long,
    var bloomTime: Long,
    var brewWater: Int,
    var bloomWater: Int,
    var remainingWater: Int,
    var withBloom: Boolean,
) {
    data class Builder(
        var phases: MutableList<Phase>? = null,
        var currentPhase: Phase? = null,
        var brewTime: Long? = null,
        var bloomTime: Long? = null,
        var brewWater: Int? = null,
        var bloomWater: Int? = null,
        var remainingWater: Int? = null,
        var withBloom: Boolean? = null,
    ) {
        fun phases(phases: MutableList<Phase>?) = apply { this.phases = phases }
        fun withCurrentPhase(phase: Phase?) = apply { this.currentPhase = phase }
        fun brewTime(time: Long?) = apply { this.brewTime = time }
        fun brewWater(water: Int?) = apply { this.brewWater = water }
        fun bloomTime(time: Long?) = apply { this.bloomTime = time }
        fun bloomWater(water: Int?) = apply { this.bloomWater = water }
        fun withBloom(withBloom: Boolean?) = apply { this.withBloom = withBloom }
        fun remainingWater(remainingWater: Int?) = apply { this.remainingWater = remainingWater }
        fun build() = BrewPhase(
//            phases = phases!!,
            currentPhase = currentPhase!!,
            brewTime = brewTime!!,
            bloomTime = bloomTime!!,
            brewWater = brewWater!!,
            bloomWater = bloomWater!!,
            remainingWater = remainingWater!!,
            withBloom = withBloom!!
        )
    }

    @StringRes
    fun title(): Int {
        return when (currentPhase) {
            Phase.BLOOM -> R.string.bloomPhase
            Phase.BREW -> R.string.brewPhase
        }
    }

    @StringRes
    fun description(): Int {
        return when (currentPhase) {
            Phase.BLOOM -> R.string.bloomPhaseWaterText
            Phase.BREW -> if (this.withBloom) R.string.brewPhaseWithBloom else R.string.brewPhaseNoBloom
        }
    }

    fun water(): Int {
        return when (currentPhase) {
            Phase.BLOOM -> bloomWater
            Phase.BREW -> if (withBloom) brewWater else remainingWater
        }
    }

    fun time(): Long {
        return when (currentPhase) {
            Phase.BLOOM -> bloomTime
            Phase.BREW -> brewTime
        }
    }
}