package co.uk.akm.test.launchlistcr.helper

import co.uk.akm.test.launchlistcr.helper.matchers.KArgumentMatcher
import co.uk.akm.test.launchlistcr.helper.matchers.KAny
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import org.mockito.verification.VerificationMode


class KMockito {
    companion object {

        fun <T> any(dummyInstance: T) = argThat(KAny(dummyInstance))

        /**
         * Returns a non-null reference that can be safely used in defining mock behaviour.
         */
        fun <T> argThat(matcher: KArgumentMatcher<T>): T {
            return (Mockito.argThat(matcher) ?: matcher.dummyInstance())
        }

        fun <T> suspendedWhen(methodCall: suspend () -> T): OngoingStubbing<T> {
            return Mockito.`when`(runBlocking { methodCall.invoke() })
        }

        fun <T> suspendedVerify(mock: T, method: suspend T.() -> Unit) {
            runBlocking { Mockito.verify(mock).method() }
        }

        fun <T> suspendedVerify(mock: T, mode: VerificationMode, method: suspend T.() -> Unit) {
            runBlocking { Mockito.verify(mock, mode).method() }
        }
    }
}