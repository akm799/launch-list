package co.uk.akm.test.launchlistrx.app.processor

import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.app.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchListViewModel

interface LaunchListProcessor : BaseProcessor<LaunchListView, LaunchListViewModel> {

    /**
     * This method should be invoked when the parent activity/fragment is initialised.
     */
    fun getListedLaunches()

    /**
     * This method should be invoked when the user clicks on a show-launches button in the parent activity/fragment.
     */
    fun listLaunches(type: String)

    fun onLaunchesListed(list: List<Launch>)

    fun onLaunchListError(t: Throwable)
}