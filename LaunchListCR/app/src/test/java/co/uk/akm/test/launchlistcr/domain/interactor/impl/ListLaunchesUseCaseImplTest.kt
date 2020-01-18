package co.uk.akm.test.launchlistcr.domain.interactor.impl

import co.uk.akm.test.launchlistcr.domain.model.Launch
import co.uk.akm.test.launchlistcr.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistcr.domain.repo.LaunchRepository
import co.uk.akm.test.launchlistcr.helper.KMockito
import co.uk.akm.test.launchlistcr.helper.suspendedWhen
import kotlinx.coroutines.runBlocking
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
        KMockito.suspendedWhen{ repository.getLaunches() }.thenReturn(fetchedLaunches)

        val underTest = ListLaunchesUseCaseImpl(repository)
        val actual = runBlocking { underTest.listLaunches(type) }
        Assert.assertThat(actual, CoreMatchers.`is`(expected))
    }
}