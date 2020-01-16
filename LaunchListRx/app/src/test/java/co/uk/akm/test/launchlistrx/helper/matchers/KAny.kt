package co.uk.akm.test.launchlistrx.helper.matchers


class KAny<T>(private val dummyInstance: T) : AbstractArgumentMatcher<T>() {
    companion object {
        fun <T> any(dummyInstance: T) = KAny(dummyInstance).mockArgument()
    }

    override fun dummyInstance(): T = dummyInstance

    override fun matches(argument: T): Boolean = true
}