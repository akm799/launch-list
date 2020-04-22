package co.uk.akm.test.launchlistrx.app.processor.impl

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.app.processor.LaunchDetailsProcessor
import co.uk.akm.test.launchlistrx.app.ui.details.LaunchDetailsView
import co.uk.akm.test.launchlistrx.util.error.DefaultErrorResolver
import co.uk.akm.test.launchlistrx.util.error.ErrorResolver
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchDetailsViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.observers.LaunchDetailsViewModelObserver
import co.uk.akm.test.launchlistrx.app.viewmodel.observers.impl.LaunchDetailsViewModelObserverImpl
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails

class LaunchDetailsProcessorIml(
    private val errorResolver: ErrorResolver = DefaultErrorResolver()
) : BaseProcessorImpl<LaunchDetailsView, LaunchDetailsViewModel, LaunchDetailsViewModelObserver>(), LaunchDetailsProcessor {

    override fun init(owner: LifecycleOwner, viewModel: LaunchDetailsViewModel) {
        observer = LaunchDetailsViewModelObserverImpl(owner, viewModel, this)
    }

    override fun getLaunchDetails(flightNumber: Int) {
        observer?.getLaunchDetails(flightNumber)
    }

    override fun onLaunchDetails(launchDetails: LaunchDetails) {
        view?.displayLaunchDetails(launchDetails)
    }

    override fun onLaunchDetailsError(t: Throwable) {
        view?.displayError(errorResolver.findErrorResId(t))
    }
}