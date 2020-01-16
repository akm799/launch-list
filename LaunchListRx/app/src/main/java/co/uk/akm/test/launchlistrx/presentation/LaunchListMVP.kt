package co.uk.akm.test.launchlistrx.presentation

import co.uk.akm.test.launchlistrx.domain.model.Launch

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