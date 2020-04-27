package co.uk.akm.test.launchlistrx.app.viewmodel.base

interface CancelableViewModelObserver {

    /**
     * Meant to be invoked if the user explicitly wishes to cancel an ongoing request.
     */
    fun cancel()
}