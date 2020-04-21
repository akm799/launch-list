package co.uk.akm.test.launchlistrx.util.date

import co.uk.akm.test.launchlistrx.domain.model.TimeInterval
import java.text.SimpleDateFormat
import java.util.*

private val utc = TimeZone.getTimeZone("UTC")
private const val DATE_FORMAT = "d MMMM yyyy"
private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX"

private const val MILLIS_IN_SEC = 1000L
private const val MILLIS_IN_MIN = 60*MILLIS_IN_SEC
private const val MILLIS_IN_HOUR = 60*MILLIS_IN_MIN

fun formatInUtc(timestamp: Long): String {
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.UK).apply { timeZone = utc }.format(Date(timestamp))
}

fun reformatAsDate(datetimeString: String): String {
    val datetime = parseDate(datetimeString)

    return SimpleDateFormat(DATE_FORMAT, Locale.UK).format(datetime)
}

fun parseDate(datetimeString: String): Date {
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.UK).parse(datetimeString) ?: throw IllegalArgumentException("Cannot parse date '$datetimeString' (parsing method returned null).")
}

fun computeTimeInterval(millis: Long): TimeInterval {
    val hours = millis/MILLIS_IN_HOUR
    val millisInHours = hours*MILLIS_IN_HOUR

    val minutes = (millis - millisInHours)/MILLIS_IN_MIN

    val seconds = (millis - millisInHours - minutes*MILLIS_IN_MIN)/ MILLIS_IN_SEC

    return TimeInterval(hours.toInt(), minutes.toInt(), seconds.toInt())
}