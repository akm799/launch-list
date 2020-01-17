package co.uk.akm.test.launchlistcr.presentation

import co.uk.akm.test.launchlistcr.domain.model.Launch

interface LaunchListMVP {

    interface View {

        fun displayLaunches(launches: List<Launch>)

        fun displayError(error: Throwable)
    }

    interface Presenter {

        fun attachView(view: View)

        fun listLaunches(type: String)

        fun cancelRequestsInProgress()

        fun detachView()
    }
}