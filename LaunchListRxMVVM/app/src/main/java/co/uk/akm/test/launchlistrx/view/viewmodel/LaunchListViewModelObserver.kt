package co.uk.akm.test.launchlistrx.view.viewmodel

interface LaunchListViewModelObserver {

    fun listLaunches(type: String)

    fun cancel()
}