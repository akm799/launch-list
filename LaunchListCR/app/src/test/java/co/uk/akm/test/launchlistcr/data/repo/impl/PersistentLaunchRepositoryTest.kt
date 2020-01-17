package co.uk.akm.test.launchlistcr.data.repo.impl

import co.uk.akm.test.launchlistcr.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistcr.data.entity.server.RocketEntity
import co.uk.akm.test.launchlistcr.data.source.LaunchCache
import co.uk.akm.test.launchlistcr.data.source.LaunchDataSource
import co.uk.akm.test.launchlistcr.helper.*
import co.uk.akm.test.launchlistcr.helper.matchers.KAny
import co.uk.akm.test.launchlistcr.helper.matchers.custom.LaunchApiEntityListMatcher
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class PersistentLaunchRepositoryTest {

    @Test
    fun shouldReadLaunches() {
        val flightNumber = 42

        val local = Mockito.mock(LaunchCache::class.java)
        val remote = Mockito.mock(LaunchDataSource::class.java)

        val entities = dbEntities(flightNumber)
        Mockito.`when`(runBlocking { local.hasLaunches() }).thenReturn(true)
        Mockito.`when`(runBlocking { local.getLaunches() }).thenReturn(entities)

        val expected = models(flightNumber)
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = runBlocking { underTest.getLaunches() }
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        runBlocking {
            Mockito.verify(remote, Mockito.never()).getLaunches()
            Mockito.verify(local, Mockito.never()).cacheLaunches(KAny.any(emptyList()))
        }
    }

    @Test
    fun shouldFetchLaunches() {
        val flightNumber = 42
        val flightNumbers = listOf(flightNumber)

        val local = Mockito.mock(LaunchCache::class.java)
        val remote = Mockito.mock(LaunchDataSource::class.java)

        val entities = apiEntities(flightNumber)
        Mockito.`when`(runBlocking { local.hasLaunches() }).thenReturn(false)
        Mockito.`when`(runBlocking { remote.getLaunches() }).thenReturn(entities)

        val expected = models(flightNumber)
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = runBlocking { underTest.getLaunches() }
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        runBlocking {
            Mockito.verify(local, Mockito.never()).getLaunches()
            Mockito.verify(local).cacheLaunches(LaunchApiEntityListMatcher(flightNumbers).mockArgument())
        }
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
        Mockito.`when`(runBlocking { local.hasLaunches() }).thenReturn(false)
        Mockito.`when`(runBlocking { remote.getLaunches() }).thenReturn(allEntities)

        val expected = models(flightNumber) // The entity without flight number is not in our expected result list.
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = runBlocking { underTest.getLaunches() }
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        runBlocking {
            Mockito.verify(local, Mockito.never()).getLaunches()
            Mockito.verify(local).cacheLaunches(LaunchApiEntityListMatcher(flightNumbers).mockArgument())
        }
    }
}