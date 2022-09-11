package gr.divinelink.core.util.extensions

import gr.divinelink.core.util.constants.Numbers.ONE_THOUSAND
import gr.divinelink.core.util.constants.Numbers.SIXTY

fun Long?.inMilliseconds(): Long {
    return this?.times(ONE_THOUSAND) ?: 0L
}

fun Long.getPairOfMinutesSeconds(): Pair<Long, Long> {
    val minutes = this / SIXTY
    val seconds = this % SIXTY
    return Pair(minutes, seconds)
}
