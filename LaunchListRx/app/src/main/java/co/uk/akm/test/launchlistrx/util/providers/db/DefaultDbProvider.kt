package co.uk.akm.test.launchlistrx.util.providers.db

import android.app.Application
import co.uk.akm.test.launchlistrx.data.db.LaunchDatabase

class DefaultDbProvider : DbProvider {

    override fun dbInstance(app: Application): LaunchDatabase = LaunchDatabase.singleInstance(app)
}