package co.uk.akm.test.launchlistrx.view.processor

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.util.error.DefaultErrorResolver
import co.uk.akm.test.launchlistrx.util.error.ErrorResolver
import co.uk.akm.test.launchlistrx.view.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchListViewModel
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchListViewModelObserver
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchListViewModelObserverImpl

class LaunchListProcessorIml(
    private val errorResolver: ErrorResolver = DefaultErrorResolver()
) : LaunchListProcessor {

    private var view: LaunchListView? = null
    private lateinit var observer: LaunchListViewModelObserver

    override fun init(owner: LifecycleOwner, viewModel: LaunchListViewModel) {
        observer = LaunchListViewModelObserverImpl(owner, viewModel, this)
    }

    override fun attachView(view: LaunchListView) {
        this.view = view
    }

    override fun listLaunches(type: String) {
        observer.listLaunches(type)
    }

    override fun onLaunchesListed(list: List<Launch>) {
        view?.displayLaunches(list)
    }

    override fun onLaunchListError(t: Throwable) {
        view?.displayError(errorResolver.findErrorResId(t))
    }

    override fun cancel() {
        observer.cancel()
    }

    override fun detachView() {
        view = null
    }
}