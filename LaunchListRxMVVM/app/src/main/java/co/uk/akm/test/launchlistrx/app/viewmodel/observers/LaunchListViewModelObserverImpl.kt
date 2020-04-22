package co.uk.akm.test.launchlistrx.app.viewmodel.observers

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.app.processor.LaunchListProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModelObserver
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchViewModel

class LaunchListViewModelObserverImpl(
    private val owner: LifecycleOwner,
    private val viewModel: LaunchViewModel,
    private val processor: LaunchListProcessor
) : BaseViewModelObserver<List<Launch>>(viewModel), LaunchListViewModelObserver {

    override fun listLaunches(type: String) {
        viewModel.listLaunches(type).observe(owner, this)
    }

    override fun onResult(result: List<Launch>) {
        processor.onLaunchesListed(result)
    }

    override fun onError(error: Throwable) {
        processor.onLaunchListError(error)
    }
}