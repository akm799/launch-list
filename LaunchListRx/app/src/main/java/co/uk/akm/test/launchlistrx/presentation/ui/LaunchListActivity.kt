package co.uk.akm.test.launchlistrx.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.akm.test.launchlistrx.BuildConfig
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.presentation.LaunchListMVP
import co.uk.akm.test.launchlistrx.presentation.ui.list.LaunchListAdapter
import co.uk.akm.test.launchlistrx.util.error.findErrorResId
import kotlinx.android.synthetic.main.activity_launch_list.*
import org.koin.android.ext.android.inject

class LaunchListActivity : AppCompatActivity(), LaunchListMVP.View {
    private val presenter: LaunchListMVP.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_list)

        bindListeners()
        initRecyclerView()
    }

    private fun bindListeners() {
        launchesRequestStatus.setRetryListener { fetchLaunches() }
    }

    private fun initRecyclerView() {
        with(launchList) {
            layoutManager = LinearLayoutManager(this@LaunchListActivity)
            adapter = LaunchListAdapter()
        }
    }

    override fun onResume() {
        super.onResume()

        presenter.attachView(this)
        fetchLaunches()
    }

    private fun fetchLaunches() {
        launchesRequestStatus.showProgress()
        presenter.listLaunches(BuildConfig.LAUNCH_TYPE)
    }

    override fun onPause() {
        super.onPause()

        presenter.cancelRequestsInProgress()
        presenter.detachView()
    }

    override fun displayLaunches(launches: List<Launch>) {
        launchesRequestStatus.showSuccess()
        (launchList.adapter as LaunchListAdapter).submitList(launches)
    }

    override fun displayError(error: Throwable) {
        Log.e(javaClass.name, "Error: ${error.message}", error)
        launchesRequestStatus.showError(findErrorResId(error))
    }
}
