package co.uk.akm.test.launchlistrx.app.viewmodel.rxobservers

import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class RxObserver<T> (private val parent: BaseViewModel<T>): SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {
        parent.onRequestInitiated(d)
    }

    override fun onSuccess(t: T) {
        parent.onRequestFinished(CallResult(t))
    }

    override fun onError(e: Throwable) {
        parent.onRequestFinished(CallResult(e))
    }
}