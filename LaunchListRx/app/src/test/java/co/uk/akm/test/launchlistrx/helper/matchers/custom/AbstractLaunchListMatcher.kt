package co.uk.akm.test.launchlistrx.helper.matchers.custom

import co.uk.akm.test.launchlistrx.helper.matchers.AbstractArgumentMatcher

abstract class AbstractLaunchListMatcher<T>(private val expectedFlightNumbers: List<Int>) : AbstractArgumentMatcher<List<T>>() {

    override fun dummyInstance(): List<T> = emptyList()

    override fun matches(argument: List<T>?): Boolean {
        if (argument == null) {
            return false
        }

        val actualFlightNumbers = argument.map { extractFlightNumber(it) }
        if (expectedFlightNumbers.size != actualFlightNumbers.size) {
            return false
        }

        for (i in 0 until expectedFlightNumbers.size) {
            if (expectedFlightNumbers[i] != actualFlightNumbers[i]) {
                return false
            }
        }

        return true
    }

    abstract fun extractFlightNumber(item: T): Int?
}