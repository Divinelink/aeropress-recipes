package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.generaterecipe.models.Recipe

class TimerTransferableModel {
    var recipe: Recipe? = null
    var brew: BrewPhase? = null
    var currentBrewState: BrewState? = null
}
