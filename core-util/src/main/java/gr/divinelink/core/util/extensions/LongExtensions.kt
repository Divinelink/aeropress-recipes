package gr.divinelink.core.util.extensions

fun Long.inMilliseconds(): Long {
    return this * 1000
}

fun Long.getPairOfMinutesSeconds(): Pair<Long, Long> {
    val minutesUntilFinished = this / 60
    val secondsInMinuteUntilFinished = this - minutesUntilFinished * 60
    return Pair(minutesUntilFinished, secondsInMinuteUntilFinished)
}