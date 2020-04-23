package co.uk.akm.test.launchlistrx.app.ui.details

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.app.processor.LaunchDetailsProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchDetailsViewModel
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails
import kotlinx.android.synthetic.main.activity_launch_details.*
import org.koin.android.ext.android.inject
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
    private val processor: LaunchDetailsProcessor by inject()

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
