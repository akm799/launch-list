package co.uk.akm.test.launchlistrx.data.api

import co.uk.akm.test.launchlistrx.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistrx.data.entity.server.LaunchDetailsApiEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface LaunchService {

    @GET("/v3/launches")
    fun getLaunches(): Single<List<LaunchApiEntity>>

    @GET("/v3/launches/{flight_number}")
    fun getLaunchDetails(@Path("flight_number") flightNumber: Int): Single<LaunchDetailsApiEntity>
}