package co.uk.akm.test.launchlistrx.util.error

interface ErrorResolver {

    fun findErrorResId(error: Throwable): Int
}