package co.uk.akm.test.launchlistrx.app.viewmodel

import androidx.lifecycle.LiveData
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.util.providers.livedata.DefaultLiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.DefaultSchedulerProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult
import co.uk.akm.test.launchlistrx.app.viewmodel.rxobservers.RxObserver
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails

class LaunchDetailsViewModel(
    private val useCase: ListLaunchesUseCase,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
    liveDataProvider: LiveDataProvider = DefaultLiveDataProvider()
) : BaseViewModel<LaunchDetails>(liveDataProvider) {

    fun getLaunchDetails(flightNumber: Int): LiveData<CallResult<LaunchDetails>> {
        return liveData.apply {
            useCase.getLaunchDetails(flightNumber)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(RxObserver(this@LaunchDetailsViewModel))
        }
    }
}