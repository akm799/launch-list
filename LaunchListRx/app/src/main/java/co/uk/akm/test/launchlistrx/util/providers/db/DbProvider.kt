package co.uk.akm.test.launchlistrx.util.providers.db

import android.app.Application
import co.uk.akm.test.launchlistrx.data.db.LaunchDatabase

interface DbProvider {

    fun dbInstance(app: Application): LaunchDatabase
}