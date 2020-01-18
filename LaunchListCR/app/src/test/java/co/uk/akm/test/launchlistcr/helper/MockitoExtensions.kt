package co.uk.akm.test.launchlistcr.helper

import kotlinx.coroutines.runBlocking
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import org.mockito.verification.VerificationMode


class KMockito {
    companion object {

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