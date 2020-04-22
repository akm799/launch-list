package co.uk.akm.test.launchlistrx.app.viewmodel.observers.impl

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.app.processor.LaunchDetailsProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchDetailsViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModelObserver
import co.uk.akm.test.launchlistrx.app.viewmodel.observers.LaunchDetailsViewModelObserver
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails

class LaunchDetailsViewModelObserverImpl(
    private val owner: LifecycleOwner,
    private val viewModel: LaunchDetailsViewModel,
    private val processor: LaunchDetailsProcessor
) : BaseViewModelObserver<LaunchDetails>(viewModel), LaunchDetailsViewModelObserver {

    override fun getLaunchDetails(flightNumber: Int) {
        viewModel.getLaunchDetails(flightNumber).observe(owner, this)
    }

    override fun onResult(result: LaunchDetails) {
        processor.onLaunchDetails(result)
    }

    override fun onError(error: Throwable) {
        processor.onLaunchDetailsError(error)
    }
}