package co.uk.akm.test.launchlistrx.data.source

import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity
import io.reactivex.Single

interface LaunchCache : LaunchDataSource {

    fun hasLaunches(): Single<Boolean>

    fun cacheLaunches(launches: List<LaunchEntity>)
}