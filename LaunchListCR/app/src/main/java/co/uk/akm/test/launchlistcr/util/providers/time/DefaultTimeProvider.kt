package co.uk.akm.test.launchlistcr.util.providers.time

class DefaultTimeProvider : TimeProvider {

    override fun now(): Long = System.currentTimeMillis()
}