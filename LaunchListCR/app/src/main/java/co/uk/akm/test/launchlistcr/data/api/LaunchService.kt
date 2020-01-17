package co.uk.akm.test.launchlistcr.data.api

import co.uk.akm.test.launchlistcr.data.entity.server.LaunchApiEntity
import retrofit2.Call
import retrofit2.http.GET

interface LaunchService {

    @GET("/v3/launches")
    fun getLaunches(): Call<List<LaunchApiEntity>>
}