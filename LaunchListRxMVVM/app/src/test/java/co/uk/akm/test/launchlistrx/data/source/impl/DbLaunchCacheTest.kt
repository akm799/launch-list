package co.uk.akm.test.launchlistrx.data.source.impl

import co.uk.akm.test.launchlistrx.data.db.LaunchDao
import co.uk.akm.test.launchlistrx.data.db.LaunchDatabase
import co.uk.akm.test.launchlistrx.helper.KMockito
import co.uk.akm.test.launchlistrx.helper.apiEntities
import co.uk.akm.test.launchlistrx.helper.dbEntities
import co.uk.akm.test.launchlistrx.helper.matchers.custom.LaunchDbEntityListMatcher
import co.uk.akm.test.launchlistrx.helper.providers.TestTimeProvider
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class DbLaunchCacheTest {
    private companion object {
        const val SECS_TO_MILLIS = 1000L
        const val LAUNCH_LIST_CACHE_ID = "launch_list"
    }

    @Test
    fun shouldNotHaveLaunchesWhenEmpty() {
        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getTimeStampCount(LAUNCH_LIST_CACHE_ID)).thenReturn(Single.just(0))

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(30, dao)
        Assert.assertFalse(underTest.hasLaunches().blockingGet())
    }

    @Test
    fun shouldNotHaveLaunchesWhenExpired() {
        val now = 100*SECS_TO_MILLIS
        val expiryTimeInSecs = 10
        val expiredCacheTime = now - (expiryTimeInSecs + expiryTimeInSecs)*SECS_TO_MILLIS

        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getTimeStampCount(LAUNCH_LIST_CACHE_ID)).thenReturn(Single.just(1))
        Mockito.`when`(dao.getTimeStamp(LAUNCH_LIST_CACHE_ID)).thenReturn(Single.just(expiredCacheTime))

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(expiryTimeInSecs, dao, TestTimeProvider(now))
        Assert.assertFalse(underTest.hasLaunches().blockingGet())
    }

    @Test
    fun shouldNotHaveLaunches() {
        val now = 100*SECS_TO_MILLIS
        val expiryTimeInSecs = 10
        val validCacheTime = now - (expiryTimeInSecs/2)*SECS_TO_MILLIS

        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getTimeStampCount(LAUNCH_LIST_CACHE_ID)).thenReturn(Single.just(1))
        Mockito.`when`(dao.getTimeStamp(LAUNCH_LIST_CACHE_ID)).thenReturn(Single.just(validCacheTime))

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(expiryTimeInSecs, dao, TestTimeProvider(now))
        Assert.assertTrue(underTest.hasLaunches().blockingGet())
    }

    @Test
    fun shouldGetLaunches() {
        val expected = dbEntities(42)
        val dao = Mockito.mock(LaunchDao::class.java)
        Mockito.`when`(dao.getLaunches()).thenReturn(Single.just(expected))

        val db = Mockito.mock(LaunchDatabase::class.java)
        Mockito.`when`(db.launchDao()).thenReturn(dao)

        val underTest = DbLaunchCache(30, dao)
        val actual = underTest.getLaunches().blockingGet()

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
        underTest.cacheLaunches(entities)

        Mockito.verify(dao).cacheLaunches(KMockito.argThat(LaunchDbEntityListMatcher(flightNumbers)))
    }
}