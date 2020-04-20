package co.uk.akm.test.launchlistrx.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.util.providers.livedata.DefaultLiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.DefaultSchedulerProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import io.reactivex.disposables.Disposable

class LaunchListViewModel(
    private val useCase: ListLaunchesUseCase,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
    private val liveDataProvider: LiveDataProvider = DefaultLiveDataProvider()
) : ViewModel() {

    private var requestInProgress: Disposable? = null

    fun listLaunches(type: String): LiveData<CallResult<List<Launch>>> {
        return liveDataProvider.liveDataInstance<List<Launch>>().apply {
            useCase.listLaunches(type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(LaunchRxObserver(this@LaunchListViewModel, this))
        }
    }

    fun onRequestInitiated(request: Disposable) {
        requestInProgress = request
    }

    fun cancelRequestsInProgress() {
        requestInProgress?.dispose()
        requestInProgress = null
    }

    fun onRequestFinished() {
        requestInProgress = null
    }
}