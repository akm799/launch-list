package co.uk.akm.test.launchlistrx.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.akm.test.launchlistrx.BuildConfig
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.view.processor.LaunchListProcessor
import co.uk.akm.test.launchlistrx.view.ui.list.LaunchListAdapter
import co.uk.akm.test.launchlistrx.view.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchListViewModel
import kotlinx.android.synthetic.main.activity_launch_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchListActivity : AppCompatActivity(), LaunchListView {
    private val viewModel: LaunchListViewModel by viewModel()
    private val processor: LaunchListProcessor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_list)

        processor.init(this, viewModel)

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

        processor.attachView(this)
        fetchLaunches()
    }

    private fun fetchLaunches() {
        launchesRequestStatus.showProgress()
        processor.listLaunches(BuildConfig.LAUNCH_TYPE)
    }

    override fun onPause() {
        super.onPause()

        processor.also {
            it.cancel()
            it.detachView()
        }
    }

    override fun displayLaunches(launches: List<Launch>) {
        launchesRequestStatus.showSuccess()
        (launchList.adapter as LaunchListAdapter).submitList(launches)
    }

    override fun displayError(errorResId: Int) {
        launchesRequestStatus.showError(errorResId)
    }
}
