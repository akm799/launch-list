package co.uk.akm.test.launchlistrx.view.viewmodel

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.domain.model.Launch
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class LaunchRxObserver (
    private val parent: LaunchListViewModel,
    private val liveData: MutableLiveData<CallResult<List<Launch>>>
): SingleObserver<List<Launch>> {

    override fun onSubscribe(d: Disposable) {
        parent.onRequestInitiated(d)
    }

    override fun onSuccess(t: List<Launch>) {
        parent.onRequestFinished()
        liveData.value = CallResult(t)
    }

    override fun onError(e: Throwable) {
        parent.onRequestFinished()
        liveData.value = CallResult(e)
    }
}