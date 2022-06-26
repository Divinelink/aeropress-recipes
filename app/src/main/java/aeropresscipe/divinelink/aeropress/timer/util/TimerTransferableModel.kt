package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.generaterecipe.Recipe
import aeropresscipe.divinelink.aeropress.timer.Phase

class TimerTransferableModel {
    var recipe: Recipe? = null

    var phases: MutableList<Phase> = mutableListOf()

    var brewTime: Long? = null
    var bloomTime: Long? = null
}