package co.uk.akm.test.launchlistrx.app.processor

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.app.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchViewModel

interface LaunchListProcessor {

    fun init(owner: LifecycleOwner, viewModel: LaunchViewModel)

    fun attachView(view: LaunchListView)

    fun listLaunches(type: String)

    fun onLaunchesListed(list: List<Launch>)

    fun onLaunchListError(t: Throwable)

    fun cancel()

    fun detachView()

    fun clear()
}