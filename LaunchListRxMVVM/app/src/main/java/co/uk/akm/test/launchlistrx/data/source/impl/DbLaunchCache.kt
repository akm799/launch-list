package co.uk.akm.test.launchlistrx.data.source.impl

import co.uk.akm.test.launchlistrx.data.db.LaunchDao
import co.uk.akm.test.launchlistrx.data.entity.LaunchDetailsEntity
import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDetailsDbEntity
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

        return existsInCache(contentCheck, expiryCheck)
    }

    override fun getLaunches(): Single<List<LaunchEntity>> {
        return dao.getLaunches().map { entities -> entities.map { it as LaunchEntity } }
    }

    override fun cacheLaunches(launches: List<LaunchEntity>) {
        dao.cacheLaunches(launches.map { LaunchDbEntity(it) })
    }

    override fun hasLaunchDetails(flightNumber: Int): Single<Boolean> {
        val contentCheck: () -> Single<Boolean> = { dao.hasLaunchDetailsCacheTime(flightNumber) }
        val expiryCheck: () -> Single<Boolean> = { dao.getLaunchDetailsCacheTime(flightNumber).map { notExpired(it) } }

        return existsInCache(contentCheck, expiryCheck)
    }

    override fun getLaunchDetails(flightNumber: Int): Single<LaunchDetailsEntity> {
        return dao.getLaunchDetails(flightNumber).map { it as LaunchDetailsEntity }
    }

    override fun cacheLaunchDetails(launchDetails: LaunchDetailsEntity) {
        dao.cacheLaunchDetails(LaunchDetailsDbEntity(launchDetails))
    }

    private fun notExpired(timestamp: Long): Boolean {
        return (timeProvider.now() - timestamp <= expiryTimeInMillis)
    }

    private fun existsInCache(contentCheck: () -> Single<Boolean>, expiryCheck: () -> Single<Boolean>): Single<Boolean> {
        val expiryCheckIfNeeded: (Boolean) -> Single<Boolean> = { contentCheckPassed ->
            if (contentCheckPassed.not()) {
                Single.just(false)
            } else {
                expiryCheck.invoke()
            }
        }

        return contentCheck.invoke().flatMap { expiryCheckIfNeeded.invoke(it) }
    }
}