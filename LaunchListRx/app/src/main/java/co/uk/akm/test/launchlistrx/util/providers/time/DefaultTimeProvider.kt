package co.uk.akm.test.launchlistrx.util.providers.time

class DefaultTimeProvider : TimeProvider {

    override fun now(): Long = System.currentTimeMillis()
}