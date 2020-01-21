package co.uk.akm.test.launchlistcr.util.error

interface ErrorResolver {

    fun findErrorMessageResId(error: Throwable): Int
}