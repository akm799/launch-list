package co.uk.akm.test.launchlistrx.helper.providers

import co.uk.akm.test.launchlistrx.util.providers.time.TimeProvider

class TestTimeProvider(private val timestamp: Long) : TimeProvider {

    override fun now(): Long = timestamp
}