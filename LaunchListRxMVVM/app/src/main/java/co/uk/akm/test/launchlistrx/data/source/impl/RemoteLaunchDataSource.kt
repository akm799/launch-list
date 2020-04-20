package co.uk.akm.test.launchlistrx.data.source.impl

import co.uk.akm.test.launchlistrx.data.api.LaunchService
import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity
import co.uk.akm.test.launchlistrx.data.source.LaunchDataSource
import io.reactivex.Single

class RemoteLaunchDataSource(private val service: LaunchService) : LaunchDataSource {

    override fun getLaunches(): Single<List<LaunchEntity>> {
        return service.getLaunches().map { entities -> entities.map { it as LaunchEntity } }
    }
}