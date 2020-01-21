package co.uk.akm.test.launchlistcr.data.source.impl

import co.uk.akm.test.launchlistcr.data.db.LaunchDao
import co.uk.akm.test.launchlistcr.data.db.LaunchDatabase
import co.uk.akm.test.launchlistcr.helper.KMockito
import co.uk.akm.test.launchlistcr.helper.apiEntities
import co.uk.akm.test.launchlistcr.helper.dbEntities
import co.uk.akm.test.launchlistcr.helper.matchers.custom.LaunchDbEntityListMatcher
import co.uk.akm.test.launchlistcr.helper.providers.TestTimeProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class DbLaunchCacheTest {
    private companion object {
        const val SECS_TO_MILLIS = 1000L
        const val LAUNCH_ENTITY_NAME = "launch"
    }

    @Test
    fun shouldNotHaveLaunchesWhenEmpty() {
        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getTimeStampCount(LAUNCH_ENTITY_NAME)).thenReturn(0)

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(30, dao)
        Assert.assertFalse(runBlocking { underTest.hasLaunches() })
    }

    @Test
    fun shouldNotHaveLaunchesWhenExpired() {
        val now = 100*SECS_TO_MILLIS
        val expiryTimeInSecs = 10
        val expiredCacheTime = now - (expiryTimeInSecs + expiryTimeInSecs)*SECS_TO_MILLIS

        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getTimeStampCount(LAUNCH_ENTITY_NAME)).thenReturn(1)
        Mockito.`when`(dao.getTimeStamp(LAUNCH_ENTITY_NAME)).thenReturn(expiredCacheTime)

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(expiryTimeInSecs, dao, TestTimeProvider(now))
        Assert.assertFalse(runBlocking { underTest.hasLaunches() })
    }

    @Test
    fun shouldNotHaveLaunches() {
        val now = 100*SECS_TO_MILLIS
        val expiryTimeInSecs = 10
        val validCacheTime = now - (expiryTimeInSecs/2)*SECS_TO_MILLIS

        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getTimeStampCount(LAUNCH_ENTITY_NAME)).thenReturn(1)
        Mockito.`when`(dao.getTimeStamp(LAUNCH_ENTITY_NAME)).thenReturn(validCacheTime)

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(expiryTimeInSecs, dao, TestTimeProvider(now))
        Assert.assertTrue(runBlocking { underTest.hasLaunches() })
    }

    @Test
    fun shouldGetLaunches() {
        val expected = dbEntities(42)
        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getLaunches()).thenReturn(expected)

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(30, dao)
        val actual = runBlocking { underTest.getLaunches() }

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun shouldCacheLaunches() {
        val flightNumber = 42
        val flightNumbers = listOf(flightNumber)
        val entities = apiEntities(flightNumber)

        val dao = Mockito.mock(LaunchDao::class.java)
        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(30, dao)
        runBlocking { underTest.cacheLaunches(entities) }

        Mockito.verify(dao).cacheLaunches(KMockito.argThat(LaunchDbEntityListMatcher(flightNumbers)))
    }
}