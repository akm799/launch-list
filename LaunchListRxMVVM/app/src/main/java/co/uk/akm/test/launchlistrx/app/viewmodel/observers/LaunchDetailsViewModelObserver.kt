package co.uk.akm.test.launchlistrx.app.viewmodel.observers

import co.uk.akm.test.launchlistrx.app.viewmodel.base.CancelableViewModelObserver

interface LaunchDetailsViewModelObserver : CancelableViewModelObserver {

    fun getLaunchDetails(flightNumber: Int)
}