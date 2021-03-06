package co.uk.akm.test.launchlistcr.presentation.presenter

import co.uk.akm.test.launchlistcr.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistcr.domain.model.Launch
import co.uk.akm.test.launchlistcr.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistcr.helper.KMockito
import co.uk.akm.test.launchlistcr.helper.providers.TestDispatcherProvider
import co.uk.akm.test.launchlistcr.presentation.LaunchListMVP
import co.uk.akm.test.launchlistcr.util.error.ErrorResolver
import org.junit.Test
import org.mockito.Mockito

class LaunchListPresenterTest {
    private val type = "falcon9"
    private val launches = listOf<Launch>(LaunchData(42, type, null, null, null, null))

    @Test
    fun shouldShowFetchedLaunches() {
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        KMockito.suspendedWhen { useCase.listLaunches(type) }.thenReturn(launches)

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val underTest = LaunchListPresenter(useCase, TestDispatcherProvider())

        underTest.attachView(view)
        underTest.listLaunches(type)

        Mockito.verify(view).displayLaunches(launches)
        Mockito.verify(view, Mockito.never()).displayError(Mockito.anyInt())
    }

    @Test
    fun shouldShowLaunchFetchError() {
        val error = RuntimeException("Launch fetch error.")
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        KMockito.suspendedWhen { useCase.listLaunches(type) }.thenThrow(error)

        val errorResId = 42
        val errorResolver = Mockito.mock(ErrorResolver::class.java)
        Mockito.`when`(errorResolver.findErrorMessageResId(error)).thenReturn(errorResId)

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val underTest = LaunchListPresenter(useCase, TestDispatcherProvider(), errorResolver)

        underTest.attachView(view)
        underTest.listLaunches(type)

        Mockito.verify(view).displayError(errorResId)
        Mockito.verify(view, Mockito.never()).displayLaunches(KMockito.any(emptyList()))
    }

    @Test
    fun shouldNotDisplayFetchedLaunchesWhenViewDetaches() {
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        KMockito.suspendedWhen { useCase.listLaunches(type) }.thenReturn(launches)

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val underTest = LaunchListPresenter(useCase, TestDispatcherProvider())

        underTest.attachView(view)
        underTest.detachView()
        underTest.listLaunches(type)

        Mockito.verify(view, Mockito.never()).displayLaunches(KMockito.any(emptyList()))
        Mockito.verify(view, Mockito.never()).displayError(Mockito.anyInt())
    }
}