package aeropresscipe.divinelink.aeropress.timer.util

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain

data class TimerTransferableModel (
    val dice: DiceDomain?,
    val brewTime: Long,
    val bloomTime: Long,
)