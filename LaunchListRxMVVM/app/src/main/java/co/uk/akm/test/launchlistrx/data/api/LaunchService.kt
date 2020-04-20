package co.uk.akm.test.launchlistrx.data.api

import co.uk.akm.test.launchlistrx.data.entity.server.LaunchApiEntity
import io.reactivex.Single
import retrofit2.http.GET

interface LaunchService {

    @GET("/v3/launches")
    fun getLaunches(): Single<List<LaunchApiEntity>>
}