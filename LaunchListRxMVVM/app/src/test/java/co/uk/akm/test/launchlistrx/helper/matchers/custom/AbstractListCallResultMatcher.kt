package co.uk.akm.test.launchlistrx.helper.matchers.custom

import co.uk.akm.test.launchlistrx.helper.matchers.KArgumentMatcher
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult

abstract class AbstractListCallResultMatcher<T>(private val expected: List<T>) : KArgumentMatcher<CallResult<List<T>>>() {

    override fun dummyInstance(): CallResult<List<T>> =
        CallResult(emptyList())

    override fun matches(argument: CallResult<List<T>>?): Boolean {
        if (argument == null) {
            return false
        }

        if (argument.hasError()) {
            return false
        }

        val actual = argument.result()

        if (expected.size != actual.size) {
            return false
        }

        for (i in 0 until expected.size) {
            if (itemMatches(expected[i], actual[i]).not()) {
                return false
            }
        }

        return true
    }

    abstract fun itemMatches(expected: T, actual: T): Boolean
}