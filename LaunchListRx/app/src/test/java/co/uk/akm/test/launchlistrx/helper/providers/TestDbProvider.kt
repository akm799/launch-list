package co.uk.akm.test.launchlistrx.helper.providers

import android.app.Application
import co.uk.akm.test.launchlistrx.data.db.LaunchDatabase
import co.uk.akm.test.launchlistrx.util.providers.db.DbProvider

class TestDbProvider(private val db: LaunchDatabase) : DbProvider {

    override fun dbInstance(app: Application): LaunchDatabase = db
}