package co.uk.akm.test.launchlistrx.data.repo.impl

import co.uk.akm.test.launchlistrx.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistrx.data.entity.server.RocketEntity
import co.uk.akm.test.launchlistrx.data.source.LaunchCache
import co.uk.akm.test.launchlistrx.data.source.LaunchDataSource
import co.uk.akm.test.launchlistrx.helper.*
import co.uk.akm.test.launchlistrx.helper.matchers.custom.LaunchApiEntityListMatcher
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito

class PersistentLaunchRepositoryTest {

    @Test
    fun shouldReadLaunches() {
        val flightNumber = 42

        val local = Mockito.mock(LaunchCache::class.java)
        val remote = Mockito.mock(LaunchDataSource::class.java)

        val entities = dbEntities(flightNumber)
        Mockito.`when`(local.hasLaunches()).thenReturn(Single.just(true))
        Mockito.`when`(local.getLaunches()).thenReturn(Single.just(entities))

        val expected = models(flightNumber)
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = underTest.getLaunches().blockingGet()
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        Mockito.verify(remote, Mockito.never()).getLaunches()
        Mockito.verify(local, Mockito.never()).cacheLaunches(KMockito.any(emptyList()))
    }

    @Test
    fun shouldFetchLaunches() {
        val flightNumber = 42
        val flightNumbers = listOf(flightNumber)

        val local = Mockito.mock(LaunchCache::class.java)
        val remote = Mockito.mock(LaunchDataSource::class.java)

        val entities = apiEntities(flightNumber)
        Mockito.`when`(local.hasLaunches()).thenReturn(Single.just(false))
        Mockito.`when`(remote.getLaunches()).thenReturn(Single.just(entities))

        val expected = models(flightNumber)
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = underTest.getLaunches().blockingGet()
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        Mockito.verify(local, Mockito.never()).getLaunches()
        Mockito.verify(local).cacheLaunches(KMockito.argThat(LaunchApiEntityListMatcher(flightNumbers)))
    }

    @Test
    fun shouldNotFetchLaunchesWithoutFlightNumber() {
        val flightNumber = 42
        val flightNumbers = listOf(flightNumber)

        val noFlightNumberEntity = LaunchApiEntity(
            null,
            "name",
            System.currentTimeMillis(),
            null,
            RocketEntity("falcon9"),
            false,
            null)

        val local = Mockito.mock(LaunchCache::class.java)
        val remote = Mockito.mock(LaunchDataSource::class.java)

        val allEntities = apiEntities(flightNumber) + noFlightNumberEntity
        Mockito.`when`(local.hasLaunches()).thenReturn(Single.just(false))
        Mockito.`when`(remote.getLaunches()).thenReturn(Single.just(allEntities))

        val expected = models(flightNumber) // The entity without flight number is not in our expected result list.
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = underTest.getLaunches().blockingGet()
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        Mockito.verify(local, Mockito.never()).getLaunches()
        Mockito.verify(local).cacheLaunches(KMockito.argThat(LaunchApiEntityListMatcher(flightNumbers)))
    }
}