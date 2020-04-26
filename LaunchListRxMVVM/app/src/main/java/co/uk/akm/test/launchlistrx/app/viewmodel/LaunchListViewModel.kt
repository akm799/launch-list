package co.uk.akm.test.launchlistrx.app.viewmodel

import androidx.lifecycle.LiveData
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.util.providers.livedata.DefaultLiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.DefaultSchedulerProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult
import co.uk.akm.test.launchlistrx.app.viewmodel.rxobservers.RxObserver

class LaunchListViewModel(
    private val useCase: ListLaunchesUseCase,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
    liveDataProvider: LiveDataProvider = DefaultLiveDataProvider()
) : BaseViewModel<List<Launch>>(liveDataProvider) {

    fun listLaunches(type: String): LiveData<CallResult<List<Launch>>> {
        return liveData.apply {
            useCase.listLaunches(type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(RxObserver(this@LaunchListViewModel))
        }
    }
}