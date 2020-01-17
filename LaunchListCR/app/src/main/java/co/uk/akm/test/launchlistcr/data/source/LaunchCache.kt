package co.uk.akm.test.launchlistcr.data.source

import co.uk.akm.test.launchlistcr.data.entity.LaunchEntity

interface LaunchCache : LaunchDataSource {

    suspend fun hasLaunches(): Boolean

    suspend fun cacheLaunches(launches: List<LaunchEntity>)
}