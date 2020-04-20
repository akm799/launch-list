package co.uk.akm.test.launchlistrx.view.viewmodel

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistrx.helper.matchers.custom.ErrorCallResultMatcher
import co.uk.akm.test.launchlistrx.helper.matchers.custom.LaunchListCallResultMatcher
import co.uk.akm.test.launchlistrx.helper.providers.TestLiveDataProvider
import co.uk.akm.test.launchlistrx.helper.providers.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mockito
import java.lang.Exception

class LaunchListViewModelTest {
    private val type = "falcon9"
    private val launches = listOf<Launch>(LaunchData(42, type, null, null, null, null))

    @Test
    fun shouldSetFetchLunches() {
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        Mockito.`when`(useCase.listLaunches(type)).thenReturn(Single.just(launches))

        val io = TestScheduler()
        val ui = TestScheduler()
        val liveData: MutableLiveData<CallResult<List<Launch>>> = Mockito.mock(MutableLiveData::class.java) as MutableLiveData<CallResult<List<Launch>>>
        val underTest = LaunchListViewModel(useCase, TestSchedulerProvider(io, ui), TestLiveDataProvider(liveData))

        underTest.listLaunches(type)
        io.triggerActions()
        ui.triggerActions()

        val matcher = LaunchListCallResultMatcher(launches)
        Mockito.verify(liveData).value = Mockito.argThat(matcher)
    }

    @Test
    fun shouldSetFetchLunchesError() {
        val error = Exception("Test error when fetching launches.")
        val useCase = Mockito.mock(ListLaunchesUseCase::class.java)
        Mockito.`when`(useCase.listLaunches(type)).thenReturn(Single.error(error))

        val io = TestScheduler()
        val ui = TestScheduler()
        val liveData: MutableLiveData<CallResult<List<Launch>>> = Mockito.mock(MutableLiveData::class.java) as MutableLiveData<CallResult<List<Launch>>>
        val underTest = LaunchListViewModel(useCase, TestSchedulerProvider(io, ui), TestLiveDataProvider(liveData))

        underTest.listLaunches(type)
        io.triggerActions()
        ui.triggerActions()

        val matcher = ErrorCallResultMatcher<List<Launch>>(error, CallResult(emptyList()))
        Mockito.verify(liveData).value = Mockito.argThat(matcher)
    }
}