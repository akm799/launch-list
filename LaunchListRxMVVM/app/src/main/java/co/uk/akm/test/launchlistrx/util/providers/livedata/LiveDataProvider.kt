package co.uk.akm.test.launchlistrx.util.providers.livedata

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.view.viewmodel.CallResult

interface LiveDataProvider {
    fun <T> liveDataInstance(): MutableLiveData<CallResult<T>>
}