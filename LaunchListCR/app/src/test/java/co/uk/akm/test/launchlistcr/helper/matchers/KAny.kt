package co.uk.akm.test.launchlistcr.helper.matchers


class KAny<T>(private val dummyInstance: T) : AbstractArgumentMatcher<T>() {

    override fun dummyInstance(): T = dummyInstance

    override fun matches(argument: T): Boolean = true
}