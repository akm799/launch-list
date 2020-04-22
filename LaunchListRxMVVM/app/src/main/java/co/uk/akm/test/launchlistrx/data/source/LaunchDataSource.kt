package co.uk.akm.test.launchlistrx.data.source

import co.uk.akm.test.launchlistrx.data.entity.LaunchDetailsEntity
import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity
import io.reactivex.Single

interface LaunchDataSource {

    fun getLaunches(): Single<List<LaunchEntity>>

    fun getLaunchDetails(flightNumber: Int): Single<LaunchDetailsEntity>
}