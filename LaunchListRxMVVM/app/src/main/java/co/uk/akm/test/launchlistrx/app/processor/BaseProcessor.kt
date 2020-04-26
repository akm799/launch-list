package co.uk.akm.test.launchlistrx.app.processor

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.app.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchListViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel

interface BaseProcessor<V, VM: BaseViewModel<*>> {

    fun init(owner: LifecycleOwner, viewModel: VM)

    fun attachView(view: V)

    fun cancel()

    fun detachView()

    fun clear()
}