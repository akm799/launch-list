package co.uk.akm.test.launchlistrx.domain.interactor.impl

import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistrx.domain.repo.LaunchRepository
import io.reactivex.Single
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class ListLaunchesUseCaseImplTest {

    @Test
    fun shouldListLaunchesOfOneType() {
        val type = "falcon9"
        val rightTypeLaunch = LaunchData(42, type, null, null, null, null)
        val wrongTypeLaunch = LaunchData(99, "wrong-type", null, null, null, null)

        val fetchedLaunches = listOf(rightTypeLaunch, wrongTypeLaunch)
        val expected = listOf<Launch>(rightTypeLaunch)

        val repository = Mockito.mock(LaunchRepository::class.java)
        Mockito.`when`(repository.getLaunches()).thenReturn(Single.just(fetchedLaunches))

        val underTest = ListLaunchesUseCaseImpl(repository)
        val actual = underTest.listLaunches(type).blockingGet()
        Assert.assertThat(actual, CoreMatchers.`is`(expected))
    }
}