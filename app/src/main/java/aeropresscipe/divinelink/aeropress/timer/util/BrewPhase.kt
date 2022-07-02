package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.R
import androidx.annotation.StringRes

enum class Phase {
    BLOOM,
    BREW,
    FINISHED
}

class BrewPhase private constructor(
    var phases: MutableList<Phase>,
//    var currentPhase: Phase,
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
        fun brewTime(time: Long?) = apply { this.brewTime = time }
        fun brewWater(water: Int?) = apply { this.brewWater = water }
        fun bloomTime(time: Long?) = apply { this.bloomTime = time }
        fun bloomWater(water: Int?) = apply { this.bloomWater = water }
        fun withBloom(withBloom: Boolean?) = apply { this.withBloom = withBloom }
        fun remainingWater(remainingWater: Int?) = apply { this.remainingWater = remainingWater }
        fun build() = BrewPhase(
            phases = phases!!,
            brewTime = brewTime!!,
            bloomTime = bloomTime!!,
            brewWater = brewWater!!,
            bloomWater = bloomWater!!,
            remainingWater = remainingWater!!,
            withBloom = withBloom!!,
        )
    }

    @StringRes
    fun title(): Int {
        return when (getCurrentPhase()) {
            Phase.BLOOM -> R.string.bloomPhase
            Phase.BREW -> R.string.brewPhase
            else -> 0
        }
    }

    @StringRes
    fun description(): Int {
        return when (getCurrentPhase()) {
            Phase.BLOOM -> R.string.bloomPhaseWaterText
            Phase.BREW -> if (this.withBloom) R.string.brewPhaseWithBloom else R.string.brewPhaseNoBloom
            else -> 0
        }
    }

    fun water(): Int {
        return when (getCurrentPhase()) {
            Phase.BLOOM -> bloomWater
            Phase.BREW -> if (withBloom) remainingWater else brewWater
            else -> 0
        }
    }

    fun time(): Long {
        return when (getCurrentPhase()) {
            Phase.BLOOM -> bloomTime
            Phase.BREW -> brewTime
            else -> 0
        }
    }

    fun getCurrentPhase(): Phase? {
        return phases.firstOrNull()
    }

    fun removeCurrentPhase() {
        phases.removeFirstOrNull()
    }
}