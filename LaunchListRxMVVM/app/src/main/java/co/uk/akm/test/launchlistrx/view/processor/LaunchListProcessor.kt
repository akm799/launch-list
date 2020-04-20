package co.uk.akm.test.launchlistrx.view.processor

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.view.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchListViewModel

interface LaunchListProcessor {

    fun init(owner: LifecycleOwner, viewModel: LaunchListViewModel)

    fun attachView(view: LaunchListView)

    fun listLaunches(type: String)

    fun onLaunchesListed(list: List<Launch>)

    fun onLaunchListError(t: Throwable)

    fun cancel()

    fun detachView()
}