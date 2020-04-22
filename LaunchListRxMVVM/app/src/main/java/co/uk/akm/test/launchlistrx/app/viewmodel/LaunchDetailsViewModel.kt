package co.uk.akm.test.launchlistrx.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : BaseViewModel() {

    private val launchDetailsLiveData: MutableLiveData<CallResult<LaunchDetails>> = liveDataProvider.liveDataInstance()

    fun getLaunchDetails(flightNumber: Int): LiveData<CallResult<LaunchDetails>> {
        return launchDetailsLiveData.apply {
            useCase.getLaunchDetails(flightNumber)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(RxObserver(this@LaunchDetailsViewModel, this))
        }
    }
}