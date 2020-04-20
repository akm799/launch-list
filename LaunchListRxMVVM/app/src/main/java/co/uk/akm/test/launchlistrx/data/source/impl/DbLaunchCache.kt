package co.uk.akm.test.launchlistrx.data.source.impl

import co.uk.akm.test.launchlistrx.data.db.LaunchDao
import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.source.LaunchCache
import co.uk.akm.test.launchlistrx.util.providers.time.DefaultTimeProvider
import co.uk.akm.test.launchlistrx.util.providers.time.TimeProvider
import io.reactivex.Single

class DbLaunchCache(
    expiryTimeInSecs: Int,
    private val dao: LaunchDao,
    private val timeProvider: TimeProvider = DefaultTimeProvider()
) : LaunchCache {
    private companion object {
        const val SECS_TO_MILLIS = 1000L
    }

    private val expiryTimeInMillis = expiryTimeInSecs*SECS_TO_MILLIS

    override fun hasLaunches(): Single<Boolean> {
        val contentCheck: () -> Single<Boolean> = { dao.hasLaunchesCacheTime() }
        val expiryCheck: () -> Single<Boolean> = { dao.getLaunchesCacheTime().map { notExpired(it) } }

        val expiryCheckIfNeeded: (Boolean) -> Single<Boolean> = { contentCheckPassed ->
            if (contentCheckPassed.not()) {
                Single.just(false)
            } else {
                expiryCheck.invoke()
            }
        }

        return contentCheck.invoke().flatMap { expiryCheckIfNeeded.invoke(it) }
    }

    private fun notExpired(timestamp: Long): Boolean {
        return (timeProvider.now() - timestamp <= expiryTimeInMillis)
    }

    override fun getLaunches(): Single<List<LaunchEntity>> {
        return dao.getLaunches().map { entities -> entities.map { it as LaunchEntity } }
    }

    override fun cacheLaunches(launches: List<LaunchEntity>) {
        dao.cacheLaunches(launches.map { LaunchDbEntity(it) })
    }
}