package co.uk.akm.test.launchlistrx

import android.app.Application
import co.uk.akm.test.launchlistrx.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LaunchListApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LaunchListApplication)
            modules(appModule)
        }
    }
}