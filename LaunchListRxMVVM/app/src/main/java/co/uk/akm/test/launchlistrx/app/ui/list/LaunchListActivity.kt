package co.uk.akm.test.launchlistrx.app.ui.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchListStats
import co.uk.akm.test.launchlistrx.util.getStringWithArgsInBold
import co.uk.akm.test.launchlistrx.app.processor.LaunchListProcessor
import co.uk.akm.test.launchlistrx.app.processor.impl.LaunchListProcessorIml
import co.uk.akm.test.launchlistrx.app.ui.details.LaunchDetailsActivity
import co.uk.akm.test.launchlistrx.app.ui.list.adapter.LaunchListAdapter
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchListViewModel
import kotlinx.android.synthetic.main.activity_launch_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchListActivity : AppCompatActivity(), LaunchListView, LaunchListActionListener {
    private val viewModel: LaunchListViewModel by viewModel()
    private val processor: LaunchListProcessor = LaunchListProcessorIml()

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
            adapter = LaunchListAdapter(this@LaunchListActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        processor.attachView(this)
        fetchLaunches()
    }

    private fun fetchLaunches() {
        // This hack is because if we are coming back from the launch-details activity we will not get a call that the launch-list data have arrived since they have not changed.
        // TODO Find a better solution.
        if (launchList.adapter?.itemCount ?: 0 == 0) {
            launchesRequestStatus.showProgress()
        }

        processor.getListedLaunches()
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

    override fun getDetailsForLaunch(flightNumber: Int) {
        LaunchDetailsActivity.start(this, flightNumber)
    }
}
