package co.uk.akm.test.launchlistcr.helper.providers

import android.app.Application
import co.uk.akm.test.launchlistcr.data.db.LaunchDatabase
import co.uk.akm.test.launchlistcr.util.providers.db.DbProvider

class TestDbProvider(private val db: LaunchDatabase) : DbProvider {

    override fun dbInstance(app: Application): LaunchDatabase = db
}