package co.uk.akm.test.launchlistrx.app.viewmodel.observers.impl

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.app.processor.LaunchListProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModelObserver
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchListViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.observers.LaunchListViewModelObserver

class LaunchListViewModelObserverImpl(
    owner: LifecycleOwner,
    private val viewModel: LaunchListViewModel,
    private val processor: LaunchListProcessor
) : BaseViewModelObserver<List<Launch>>(owner, viewModel), LaunchListViewModelObserver {

    override fun getListedLaunches() {
        observe(viewModel.getListedLaunches())
    }

    override fun listLaunches(type: String) {
        observe(viewModel.listLaunches(type))
    }

    override fun onResult(result: List<Launch>) {
        processor.onLaunchesListed(result)
    }

    override fun onError(error: Throwable) {
        processor.onLaunchListError(error)
    }
}