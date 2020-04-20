package co.uk.akm.test.launchlistrx.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.util.providers.livedata.DefaultLiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.DefaultSchedulerProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import co.uk.akm.test.launchlistrx.view.viewmodel.rxobservers.LaunchListRxObserver
import io.reactivex.disposables.Disposable

class LaunchListViewModel(
    private val useCase: ListLaunchesUseCase,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
    liveDataProvider: LiveDataProvider = DefaultLiveDataProvider()
) : ViewModel() {

    private val launchListLiveData: MutableLiveData<CallResult<List<Launch>>> = liveDataProvider.liveDataInstance()

    private var requestInProgress: Disposable? = null

    fun listLaunches(type: String): LiveData<CallResult<List<Launch>>> {
        return launchListLiveData.apply {
            useCase.listLaunches(type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(LaunchListRxObserver(this@LaunchListViewModel, this))
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