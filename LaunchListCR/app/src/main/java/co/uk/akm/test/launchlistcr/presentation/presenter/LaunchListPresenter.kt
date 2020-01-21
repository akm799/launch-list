package co.uk.akm.test.launchlistcr.presentation.presenter

import co.uk.akm.test.launchlistcr.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistcr.domain.model.Launch
import co.uk.akm.test.launchlistcr.presentation.LaunchListMVP
import co.uk.akm.test.launchlistcr.util.cr.CallResult
import co.uk.akm.test.launchlistcr.util.error.DefaultErrorResolver
import co.uk.akm.test.launchlistcr.util.error.ErrorResolver
import co.uk.akm.test.launchlistcr.util.providers.cr.DefaultDispatcherProvider
import co.uk.akm.test.launchlistcr.util.providers.cr.DispatcherProvider
import kotlinx.coroutines.*

class LaunchListPresenter(
    private val useCase: ListLaunchesUseCase,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
    private val errorResolver: ErrorResolver = DefaultErrorResolver()
) : LaunchListMVP.Presenter {

    private var view: LaunchListMVP.View? = null
    private var requestInProgress: Job? = null

    override fun attachView(view: LaunchListMVP.View) {
        this.view = view
    }

    override fun listLaunches(type: String) {
        issueRequest({ useCase.listLaunches(type) }, { processListLaunchesResult(it) })
    }

    private fun processListLaunchesResult(result: CallResult<List<Launch>>) {
        if (result.hasResult()) {
            view?.displayLaunches(result.result())
        } else {
            view?.displayError(errorResolver.findErrorMessageResId(result.error()))
        }
    }

    override fun cancelRequestsInProgress() {
        requestInProgress?.cancel()
        requestInProgress = null
    }

    override fun detachView() {
        view = null
    }

    private fun <T> issueRequest(request: suspend () -> T, resultReceiver: (CallResult<T>) -> Unit) {
        val job = Job()
        val coroutineScope = CoroutineScope(dispatcherProvider.main() + job)

        coroutineScope.launch(dispatcherProvider.main()) {
            requestInProgress = job

            try {
                val result = ioThread { request.invoke() }
                resultReceiver.invoke(result)
            } finally {
                requestInProgress = null
            }
        }
    }

    private suspend fun <T> ioThread(f: suspend () -> T): CallResult<T> {
        return withContext(dispatcherProvider.io()) {
            try {
                CallResult(f.invoke())
            } catch (t: Throwable) {
                CallResult<T>(t)
            }
        }
    }
}