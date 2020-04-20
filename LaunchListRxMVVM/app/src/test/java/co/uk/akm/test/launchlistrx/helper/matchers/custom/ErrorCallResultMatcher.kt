package co.uk.akm.test.launchlistrx.helper.matchers.custom

import co.uk.akm.test.launchlistrx.helper.matchers.KArgumentMatcher
import co.uk.akm.test.launchlistrx.view.viewmodel.CallResult

class ErrorCallResultMatcher<T>(
    private val expected: Throwable,
    private val dummy: CallResult<T>
) : KArgumentMatcher<CallResult<T>>() {

    override fun dummyInstance(): CallResult<T> = dummy

    override fun matches(argument: CallResult<T>?): Boolean {
        if (argument == null) {
            return false
        }

        if (argument.hasResult()) {
            return false
        }

        return (expected == argument.error())
    }
}