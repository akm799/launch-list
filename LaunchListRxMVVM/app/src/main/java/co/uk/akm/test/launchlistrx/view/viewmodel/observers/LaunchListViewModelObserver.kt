package co.uk.akm.test.launchlistrx.view.viewmodel.observers

import co.uk.akm.test.launchlistrx.view.viewmodel.base.CancelableViewModelObserver

interface LaunchListViewModelObserver : CancelableViewModelObserver {

    fun listLaunches(type: String)
}