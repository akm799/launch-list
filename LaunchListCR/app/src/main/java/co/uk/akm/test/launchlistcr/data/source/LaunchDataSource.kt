package co.uk.akm.test.launchlistcr.data.source

import co.uk.akm.test.launchlistcr.data.entity.LaunchEntity
import io.reactivex.Single

interface LaunchDataSource {

    suspend fun getLaunches(): List<LaunchEntity>
}