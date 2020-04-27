package co.uk.akm.test.launchlistrx.app.ui.details

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.app.processor.LaunchDetailsProcessor
import co.uk.akm.test.launchlistrx.app.processor.impl.LaunchDetailsProcessorIml
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchDetailsViewModel
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails
import co.uk.akm.test.launchlistrx.util.date.reformatAsDateTime
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_launch_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchDetailsActivity : AppCompatActivity(), LaunchDetailsView {
    companion object {
        private const val FLIGHT_NUMBER_KEY = "LaunchDetailsActivity.Flight.Number"

        fun start(parent: Activity, flightNumber: Int) {
            val intent = Intent(parent, LaunchDetailsActivity::class.java).apply {
                putExtra(FLIGHT_NUMBER_KEY, flightNumber)
            }

            parent.startActivity(intent)
        }
    }

    private val viewModel: LaunchDetailsViewModel by viewModel()
    private val processor: LaunchDetailsProcessor = LaunchDetailsProcessorIml()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_details)

        processor.init(this, viewModel)
    }

    override fun onResume() {
        super.onResume()

        processor.attachView(this)
        processor.getLaunchDetails(getFlightNumber())
    }

    private fun getFlightNumber(): Int {
        return intent.getIntExtra(FLIGHT_NUMBER_KEY, 0)
    }

    override fun onPause() {
        try {
            processor.also {
                it.cancel()
                it.detachView()
            }
        } finally {
            super.onPause()
        }
    }

    override fun displayLaunchDetails(launchDetails: LaunchDetails) {
        launchDetailsTitle.text = launchDetails.missionName

        Glide.with(this)
            .load(launchDetails.missionPatch)
            .error(R.drawable.ic_launcher_foreground)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(launcDetailsMissionPatch)

        if (launchDetails.hasDate) {
            launchDetailsDate.text = resources.getString(R.string.launch_date, reformatAsDateTime(launchDetails.date))
        } else {
            launchDetailsDate.setText(R.string.unknown_launch_date)
        }

        if (launchDetails.hasSiteName) {
            launchDetailsSite.text =  getString(R.string.mission_launch_site, launchDetails.siteName)
        }

        if (launchDetails.hasSuccess) {
            launchDetailsOutcome.setText(if (launchDetails.success) R.string.mission_outcome_text_success else R.string.mission_outcome_text_failure)
        } else {
            launchDetailsOutcome.setText(R.string.mission_outcome_text_unknown)
        }

        if (launchDetails.hasDetails) {
            launchDetailsSummary.text = launchDetails.details
        }
    }

    override fun displayError(errorResId: Int) {
        launchDetailsTitle.setText(errorResId)
    }

    override fun onDestroy() {
        try {
            processor.clear()
        } finally {
            super.onDestroy()
        }
    }
}
