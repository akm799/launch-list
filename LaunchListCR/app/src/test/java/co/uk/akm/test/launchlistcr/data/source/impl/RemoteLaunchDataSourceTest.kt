package co.uk.akm.test.launchlistcr.data.source.impl

import co.uk.akm.test.launchlistcr.data.api.LaunchService
import co.uk.akm.test.launchlistcr.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistcr.helper.apiEntities
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Response

class RemoteLaunchDataSourceTest {

    @Test
    fun shouldGetLaunches() {
        val expected = apiEntities(42)

        val call = Mockito.mock(Call::class.java) as Call<List<LaunchApiEntity>>
        Mockito.`when`(call.execute()).thenReturn(Response.success(expected))

        val service = Mockito.mock(LaunchService::class.java)
        Mockito.`when`(service.getLaunches()).thenReturn(call)

        val underTest = RemoteLaunchDataSource(service)
        val actual = runBlocking { underTest.getLaunches() }

        Assert.assertEquals(expected, actual)
    }
}