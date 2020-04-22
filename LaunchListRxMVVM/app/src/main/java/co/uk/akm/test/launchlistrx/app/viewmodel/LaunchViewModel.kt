package co.uk.akm.test.launchlistrx.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.util.providers.livedata.DefaultLiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.DefaultSchedulerProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult
import co.uk.akm.test.launchlistrx.app.viewmodel.rxobservers.LaunchRxObserver
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails

class LaunchViewModel(
    private val useCase: ListLaunchesUseCase,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
    liveDataProvider: LiveDataProvider = DefaultLiveDataProvider()
) : BaseViewModel() {

    private val launchListLiveData: MutableLiveData<CallResult<List<Launch>>> = liveDataProvider.liveDataInstance()
    private val launchDetailsLiveData: MutableLiveData<CallResult<LaunchDetails>> = liveDataProvider.liveDataInstance()

    fun listLaunches(type: String): LiveData<CallResult<List<Launch>>> {
        return launchListLiveData.apply {
            useCase.listLaunches(type)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(LaunchRxObserver(this@LaunchViewModel, this))
        }
    }

    fun getLaunchDetails(flightNumber: Int): LiveData<CallResult<LaunchDetails>> {
        return launchDetailsLiveData.apply {
            useCase.getLaunchDetails(flightNumber)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(LaunchRxObserver(this@LaunchViewModel, this))
        }
    }
}