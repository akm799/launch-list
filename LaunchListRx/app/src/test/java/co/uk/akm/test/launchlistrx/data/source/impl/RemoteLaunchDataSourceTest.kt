package co.uk.akm.test.launchlistrx.data.source.impl

import co.uk.akm.test.launchlistrx.data.api.LaunchService
import co.uk.akm.test.launchlistrx.helper.apiEntities
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class RemoteLaunchDataSourceTest {

    @Test
    fun shouldGetLaunches() {
        val expected = apiEntities(42)

        val service = Mockito.mock(LaunchService::class.java)
        Mockito.`when`(service.getLaunches()).thenReturn(Single.just(expected))

        val underTest = RemoteLaunchDataSource(service)
        val actual = underTest.getLaunches().blockingGet()

        Assert.assertEquals(expected, actual)
    }
}