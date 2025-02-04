package aeropresscipe.divinelink.aeropress.extension

import kotlinx.datetime.Clock

fun Clock.currentEpochSeconds(): String = now().epochSeconds.toString()
