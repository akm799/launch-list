package co.uk.akm.test.launchlistcr.helper

import kotlinx.coroutines.runBlocking
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing


class KMockito {
    companion object {}
}

fun <T> KMockito.Companion.suspendedWhen(f: suspend () -> T): OngoingStubbing<T> {
    return Mockito.`when`(runBlocking { f.invoke() })
}