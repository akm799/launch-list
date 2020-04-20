package co.uk.akm.test.launchlistrx.helper.matchers


class KAny<T>(private val dummyInstance: T) : KArgumentMatcher<T>() {

    override fun dummyInstance(): T = dummyInstance

    override fun matches(argument: T): Boolean = true
}