package aeropresscipe.divinelink.aeropress.extension

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

/**
 * Extension function to format a string to a date.
 * @return the formatted date in "MMMM yyyy" format. (e.g. "February 2025")
 */
fun String.formatDate(): String {
  val epochSeconds = this.toLong()
  val instant = Instant.ofEpochSecond(epochSeconds)
  val localDateTime = instant.atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()

  val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)
  return localDateTime.format(formatter)
}
