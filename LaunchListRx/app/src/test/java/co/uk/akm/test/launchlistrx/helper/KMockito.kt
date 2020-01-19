package co.uk.akm.test.launchlistrx.helper

import co.uk.akm.test.launchlistrx.helper.matchers.KAny
import co.uk.akm.test.launchlistrx.helper.matchers.KArgumentMatcher
import org.mockito.Mockito

/**
 * Kotlin-friendly Mockito functions.
 */
class KMockito {

    companion object {

        /**
         * Kotlin-friendly "any" matcher. This matcher will always return a non-null reference that
         * keeps the Kotlin runtime happy.
         *
         * @param dummyInstance a dummy instance used to always return a non-null reference. This
         * instance is never going to be used in any tests so its value does not matter.
         *
         * @return a Kotlin-friendly matcher that matches any argument value and will never return
         * a non-null reference
         */
        fun <T> any(dummyInstance: T): T = argThat(KAny(dummyInstance))

        /**
         * Kotlin-friendly matcher. This matcher will always return a non-null reference that
         * keeps the Kotlin runtime happy.
         *
         * @param matcher the matcher that will actually perform the desired argument matching
         *
         * @return a Kotlin-friendly matcher that matches any argument value and will never return
         * a non-null reference
         *
         * @see KArgumentMatcher
         */
        fun <T> argThat(matcher: KArgumentMatcher<T>): T {
            return Mockito.argThat(matcher) ?: matcher.dummyInstance()
        }
    }
}