package co.uk.akm.test.launchlistcr.data.repo.impl

import co.uk.akm.test.launchlistcr.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistcr.data.entity.server.RocketEntity
import co.uk.akm.test.launchlistcr.data.source.LaunchCache
import co.uk.akm.test.launchlistcr.data.source.LaunchDataSource
import co.uk.akm.test.launchlistcr.helper.*
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
        KMockito.suspendedWhen { local.hasLaunches() }.thenReturn(true)
        KMockito.suspendedWhen { local.getLaunches() }.thenReturn(entities)

        val expected = models(flightNumber)
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = runBlocking { underTest.getLaunches() }
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        KMockito.suspendedVerify(remote, Mockito.never()) { getLaunches() }
        KMockito.suspendedVerify(local, Mockito.never()) { cacheLaunches(KMockito.any(emptyList())) }
    }

    @Test
    fun shouldFetchLaunches() {
        val flightNumber = 42
        val flightNumbers = listOf(flightNumber)

        val local = Mockito.mock(LaunchCache::class.java)
        val remote = Mockito.mock(LaunchDataSource::class.java)

        val entities = apiEntities(flightNumber)
        KMockito.suspendedWhen { local.hasLaunches() }.thenReturn(false)
        KMockito.suspendedWhen { remote.getLaunches() }.thenReturn(entities)

        val expected = models(flightNumber)
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = runBlocking { underTest.getLaunches() }
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        KMockito.suspendedVerify(local, Mockito.never()) { getLaunches() }
        KMockito.suspendedVerify(local) { cacheLaunches(LaunchApiEntityListMatcher(flightNumbers).mockArgument()) }
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
        KMockito.suspendedWhen { local.hasLaunches() }.thenReturn(false)
        KMockito.suspendedWhen { remote.getLaunches() }.thenReturn(allEntities)

        val expected = models(flightNumber) // The entity without flight number is not in our expected result list.
        val underTest = PersistentLaunchRepository(local, remote)
        val actual = runBlocking { underTest.getLaunches() }
        assertSame(expected, actual) { i1, i2 -> i1.flightNumber == i2.flightNumber }

        KMockito.suspendedVerify(local, Mockito.never()) { getLaunches() }
        KMockito.suspendedVerify(local) { cacheLaunches(LaunchApiEntityListMatcher(flightNumbers).mockArgument()) }
    }
}