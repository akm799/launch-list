package co.uk.akm.test.launchlistcr.util.providers.db

import android.app.Application
import co.uk.akm.test.launchlistcr.data.db.LaunchDatabase

interface DbProvider {

    fun dbInstance(app: Application): LaunchDatabase
}