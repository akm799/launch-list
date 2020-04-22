package co.uk.akm.test.launchlistrx.app.processor

import co.uk.akm.test.launchlistrx.app.ui.details.LaunchDetailsView
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchDetailsViewModel
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails

interface LaunchDetailsProcessor : BaseProcessor<LaunchDetailsView, LaunchDetailsViewModel> {

    fun getLaunchDetails(flightNumber: Int)

    fun onLaunchDetails(launchDetails: LaunchDetails)

    fun onLaunchDetailsError(t: Throwable)
}