package co.uk.akm.test.launchlistcr.data.source.impl

import co.uk.akm.test.launchlistcr.data.db.LaunchDao
import co.uk.akm.test.launchlistcr.data.entity.LaunchEntity
import co.uk.akm.test.launchlistcr.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistcr.data.source.LaunchCache
import co.uk.akm.test.launchlistcr.util.providers.time.DefaultTimeProvider
import co.uk.akm.test.launchlistcr.util.providers.time.TimeProvider

class DbLaunchCache(
    expiryTimeInSecs: Int,
    private val dao: LaunchDao,
    private val timeProvider: TimeProvider = DefaultTimeProvider()
) : LaunchCache {
    private companion object {
        const val SECS_TO_MILLIS = 1000L
    }

    private val expiryTimeInMillis = expiryTimeInSecs*SECS_TO_MILLIS

    override suspend fun hasLaunches(): Boolean {
        return dao.hasLaunchesCacheTime() && notExpired(dao.getLaunchesCacheTime())
    }

    private fun notExpired(timestamp: Long): Boolean {
        return (timeProvider.now() - timestamp <= expiryTimeInMillis)
    }

    override suspend fun getLaunches(): List<LaunchEntity> = dao.getLaunches()

    override suspend fun cacheLaunches(launches: List<LaunchEntity>) {
        dao.cacheLaunches(launches.map { LaunchDbEntity(it) })
    }
}