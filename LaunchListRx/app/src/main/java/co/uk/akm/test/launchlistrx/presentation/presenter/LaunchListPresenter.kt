package co.uk.akm.test.launchlistrx.presentation.presenter

import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.presentation.LaunchListMVP
import co.uk.akm.test.launchlistrx.util.error.DefaultErrorResolver
import co.uk.akm.test.launchlistrx.util.error.ErrorResolver
import co.uk.akm.test.launchlistrx.util.providers.rx.DefaultSchedulerProvider
import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class LaunchListPresenter(
    private val useCase: ListLaunchesUseCase,
    private val schedulerProvider: SchedulerProvider = DefaultSchedulerProvider(),
    private val errorResolver: ErrorResolver = DefaultErrorResolver()
) : LaunchListMVP.Presenter {

    private var view: LaunchListMVP.View? = null
    private var requestInProgress: Disposable? = null

    override fun attachView(view: LaunchListMVP.View) {
        this.view = view
    }

    override fun listLaunches(type: String) {
        useCase.listLaunches(type)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(LaunchListObserver())
    }

    override fun cancelRequestsInProgress() {
        requestInProgress?.dispose()
        requestInProgress = null
    }

    override fun detachView() {
        view = null
    }

    private inner class LaunchListObserver : SingleObserver<List<Launch>> {

        override fun onSubscribe(d: Disposable) {
            requestInProgress = d
        }

        override fun onSuccess(t: List<Launch>) {
            requestInProgress = null
            view?.displayLaunches(t)
        }

        override fun onError(e: Throwable) {
            requestInProgress = null
            view?.displayError(errorResolver.findErrorResId(e))
        }
    }
}