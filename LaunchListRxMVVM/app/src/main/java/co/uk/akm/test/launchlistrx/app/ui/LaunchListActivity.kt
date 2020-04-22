package co.uk.akm.test.launchlistrx.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.akm.test.launchlistrx.BuildConfig
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchListStats
import co.uk.akm.test.launchlistrx.util.getStringWithArgsInBold
import co.uk.akm.test.launchlistrx.app.processor.LaunchListProcessor
import co.uk.akm.test.launchlistrx.app.ui.list.LaunchListAdapter
import co.uk.akm.test.launchlistrx.app.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchViewModel
import kotlinx.android.synthetic.main.activity_launch_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchListActivity : AppCompatActivity(), LaunchListView {
    private val viewModel: LaunchViewModel by viewModel()
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

    override fun displayLaunches(launches: List<Launch>, statistics: LaunchListStats) {
        launchesRequestStatus.showSuccess()
        showStatistics(statistics)
        (launchList.adapter as LaunchListAdapter).submitList(launches)
    }

    private fun showStatistics(statistics: LaunchListStats) {
        launchSuccessPercentage.text = getStringWithArgsInBold(R.string.stats_success_percentage, statistics.successPercentage)
        launchMeanTimeBetween.text = getStringWithArgsInBold(
            R.string.stats_mean_time_btwn_launches,
            statistics.meanTimeBetweenLaunches.days,
            statistics.meanTimeBetweenLaunches.hours,
            statistics.meanTimeBetweenLaunches.minutes,
            statistics.meanTimeBetweenLaunches.seconds)
    }

    override fun displayError(errorResId: Int) {
        launchesRequestStatus.showError(errorResId)
    }

    override fun onDestroy() {
        try {
            processor.clear()
        } finally {
            super.onDestroy()
        }
    }
}
