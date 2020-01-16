package co.uk.akm.test.launchlistrx.presentation.presenter

import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistrx.helper.matchers.KAny
import co.uk.akm.test.launchlistrx.helper.providers.TestSchedulerProvider
import co.uk.akm.test.launchlistrx.presentation.LaunchListMVP
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito

class LaunchListPresenterTest {
    private val type = "falcon9"
    private val launches = listOf<Launch>(LaunchData(42, type, null, null, null, null))

    @Test
    fun shouldShowFetchedLaunches() {
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        Mockito.`when`(useCase.listLaunches(type)).thenReturn(Single.just(launches))

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val io = TestScheduler()
        val ui = TestScheduler()
        val underTest = LaunchListPresenter(useCase, TestSchedulerProvider(io, ui))

        underTest.attachView(view)
        underTest.listLaunches(type)
        io.triggerActions()
        ui.triggerActions()

        Mockito.verify(view).displayLaunches(launches)
        Mockito.verify(view, Mockito.never()).displayError(KAny.any(Exception()))
    }

    @Test
    fun shouldShowLaunchFetchError() {
        val error = Exception("Launch fetch error.")
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        Mockito.`when`(useCase.listLaunches(type)).thenReturn(Single.error(error))

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val io = TestScheduler()
        val ui = TestScheduler()
        val underTest = LaunchListPresenter(useCase, TestSchedulerProvider(io, ui))

        underTest.attachView(view)
        underTest.listLaunches(type)
        io.triggerActions()
        ui.triggerActions()

        Mockito.verify(view).displayError(error)
        Mockito.verify(view, Mockito.never()).displayLaunches(KAny.any(emptyList()))
    }

    @Test
    fun shouldCancelLaunchFetchRequest() {
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        Mockito.`when`(useCase.listLaunches(type)).thenReturn(Single.just(launches))

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val io = TestScheduler()
        val ui = TestScheduler()
        val underTest = LaunchListPresenter(useCase, TestSchedulerProvider(io, ui))

        underTest.attachView(view)
        underTest.listLaunches(type)
        underTest.cancelRequestsInProgress()
        io.triggerActions()
        ui.triggerActions()

        Mockito.verify(view, Mockito.never()).displayLaunches(KAny.any(emptyList()))
        Mockito.verify(view, Mockito.never()).displayError(KAny.any(Exception()))
    }

    @Test
    fun shouldNotDisplayFetchedLaunchesWhenViewDetaches() {
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        Mockito.`when`(useCase.listLaunches(type)).thenReturn(Single.just(launches))

        val view = Mockito.mock(LaunchListMVP.View::class.java)

        val io = TestScheduler()
        val ui = TestScheduler()
        val underTest = LaunchListPresenter(useCase, TestSchedulerProvider(io, ui))

        underTest.attachView(view)
        underTest.listLaunches(type)
        underTest.detachView()
        io.triggerActions()
        ui.triggerActions()

        Mockito.verify(view, Mockito.never()).displayLaunches(KAny.any(emptyList()))
        Mockito.verify(view, Mockito.never()).displayError(KAny.any(Exception()))
    }
}