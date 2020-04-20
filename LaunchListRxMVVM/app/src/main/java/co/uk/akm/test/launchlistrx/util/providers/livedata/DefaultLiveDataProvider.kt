package co.uk.akm.test.launchlistrx.util.providers.livedata

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.view.viewmodel.CallResult

class DefaultLiveDataProvider : LiveDataProvider {
    override fun <T> liveDataInstance(): MutableLiveData<CallResult<T>> = MutableLiveData()
}