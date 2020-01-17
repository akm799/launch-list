package co.uk.akm.test.launchlistcr.data.source.impl

import co.uk.akm.test.launchlistcr.data.api.LaunchService
import co.uk.akm.test.launchlistcr.data.entity.LaunchEntity
import co.uk.akm.test.launchlistcr.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistcr.data.source.LaunchDataSource
import retrofit2.HttpException

class RemoteLaunchDataSource(private val service: LaunchService) : LaunchDataSource {

    override suspend fun getLaunches(): List<LaunchEntity> {
        val response = service.getLaunches().execute()

        if (response.isSuccessful) {
            return response.body() as List<LaunchApiEntity>
        } else {
            throw HttpException(response)
        }
    }
}