package aeropresscipe.divinelink.aeropress.timer.util

import androidx.annotation.StringRes
import com.divinelink.aeropress.recipes.R

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
  open val phaseWater: Int,
  open val totalWater: Int,
  open val brewTime: Long,
  open val phase: Phase,
  open val update: Boolean,
  open val animate: Boolean,
) {
  data class Bloom(val water: Int, val time: Long) : BrewState(
    title = R.string.bloomPhase,
    description = R.string.bloomPhaseWaterText,
    phaseWater = water,
    totalWater = 0,
    time,
    Phase.Bloom,
    true,
    false,
  )

  data class Brew(val water: Int, val time: Long) : BrewState(
    title = R.string.brewPhase,
    description = R.string.brewPhaseNoBloom,
    phaseWater = water,
    totalWater = water,
    time,
    Phase.Brew,
    true,
    true,
  )

  data class BrewWithBloom(val water: Int, override val totalWater: Int, val time: Long) :
    BrewState(
      title = R.string.brewPhase,
      description = R.string.brewPhaseWithBloom,
      phaseWater = water,
      totalWater = totalWater,
      time,
      Phase.Brew,
      true,
      true,
    )

  object Finished : BrewState(
    title = R.string.finishPhaseTitle,
    description = R.string.finishPhaseDescription,
    phaseWater = 0,
    totalWater = 0,
    brewTime = 0,
    Phase.Finish,
    false,
    true,
  )
}

enum class Phase {
  Bloom,
  Brew,
  Finish
}
