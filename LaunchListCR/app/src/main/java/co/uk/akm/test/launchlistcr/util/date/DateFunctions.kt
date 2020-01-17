package co.uk.akm.test.launchlistcr.util.date

import java.text.SimpleDateFormat
import java.util.*

private val utc = TimeZone.getTimeZone("UTC")
private const val DATE_FORMAT = "d MMMM yyyy"
private const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX"

fun formatInUtc(timestamp: Long): String {
    return SimpleDateFormat(DATE_TIME_FORMAT, Locale.UK).apply { timeZone = utc }.format(Date(timestamp))
}

fun reformatAsDate(datetimeString: String): String {
    val datetime = SimpleDateFormat(DATE_TIME_FORMAT, Locale.UK).parse(datetimeString)

    return SimpleDateFormat(DATE_FORMAT, Locale.UK).format(datetime)
}