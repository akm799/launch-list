package co.uk.akm.test.launchlistcr.helper.providers

import co.uk.akm.test.launchlistcr.util.providers.time.TimeProvider

class TestTimeProvider(private val timestamp: Long) : TimeProvider {

    override fun now(): Long = timestamp
}