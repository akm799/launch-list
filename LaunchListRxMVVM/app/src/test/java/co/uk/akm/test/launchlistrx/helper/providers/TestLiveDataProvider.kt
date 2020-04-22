package co.uk.akm.test.launchlistrx.helper.providers

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult

class TestLiveDataProvider<I>(private val liveData: MutableLiveData<CallResult<I>>) : LiveDataProvider {
    override fun <T> liveDataInstance(): MutableLiveData<CallResult<T>> {
        return liveData as MutableLiveData<CallResult<T>>
    }
}