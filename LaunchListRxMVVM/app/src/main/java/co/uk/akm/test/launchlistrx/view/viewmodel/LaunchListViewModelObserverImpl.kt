package co.uk.akm.test.launchlistrx.view.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.view.processor.LaunchListProcessor

class LaunchListViewModelObserverImpl(
    private val owner: LifecycleOwner,
    private val viewModel: LaunchListViewModel,
    private val processor: LaunchListProcessor
) : Observer<CallResult<List<Launch>>>, LaunchListViewModelObserver {

    override fun listLaunches(type: String) {
        viewModel.listLaunches(type).observe(owner, this)
    }

    override fun onChanged(t: CallResult<List<Launch>>?) {
        if (t != null) {
            if (t.hasError()) {
                processor.onLaunchListError(t.error())
            } else {
                processor.onLaunchesListed(t.result())
            }
        } else {
            processor.onLaunchListError(NullPointerException("Null call-result when listing launches."))
        }
    }

    override fun cancel() {
        viewModel.cancelRequestsInProgress()
    }
}