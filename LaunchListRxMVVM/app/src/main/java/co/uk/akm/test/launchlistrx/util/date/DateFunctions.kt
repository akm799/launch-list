package co.uk.akm.test.launchlistrx.util.date

import co.uk.akm.test.launchlistrx.domain.model.TimeInterval
import java.text.SimpleDateFormat
import java.util.*

private val utc = TimeZone.getTimeZone("UTC")
private const val DATE_FORMAT = "d MMMM yyyy"
private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX"
private const val DATE_TIME_FORMAT_DISPLAY = "d MMMM yyyy HH:mm:ss XXX"

private const val MILLIS_IN_SEC = 1000L
private const val MILLIS_IN_MIN = 60*MILLIS_IN_SEC
private const val MILLIS_IN_HOUR = 60*MILLIS_IN_MIN
private const val MILLIS_IN_DAY = 24* MILLIS_IN_HOUR

fun formatInUtc(timestamp: Long): String {
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.UK).apply { timeZone = utc }.format(Date(timestamp))
}

fun reformatAsDate(datetimeString: String): String = reformatDateAs(datetimeString, DATE_FORMAT)

fun reformatAsDateTime(datetimeString: String): String = reformatDateAs(datetimeString, DATE_TIME_FORMAT_DISPLAY)

private fun reformatDateAs(datetimeString: String, dateFormat: String): String {
    val datetime = parseDate(datetimeString)

    return SimpleDateFormat(dateFormat, Locale.UK).format(datetime)
}

fun parseDate(datetimeString: String): Date {
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.UK).parse(datetimeString) ?: throw IllegalArgumentException("Cannot parse date '$datetimeString' (parsing method returned null).")
}

fun computeTimeInterval(millis: Long): TimeInterval {
    val days = millis/MILLIS_IN_DAY
    val millisInDays = days*MILLIS_IN_DAY

    val hours = (millis - millisInDays)/MILLIS_IN_HOUR
    val millisInHours = hours*MILLIS_IN_HOUR

    val minutes = (millis - millisInDays - millisInHours)/MILLIS_IN_MIN
    val millisInMinutes = minutes*MILLIS_IN_MIN

    val seconds = (millis - millisInDays - millisInHours - millisInMinutes)/MILLIS_IN_SEC

    return TimeInterval(days.toInt(), hours.toInt(), minutes.toInt(), seconds.toInt())
}