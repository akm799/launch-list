package co.uk.akm.test.launchlistrx.di

import co.uk.akm.test.launchlistrx.BuildConfig
import co.uk.akm.test.launchlistrx.data.api.LaunchService
import co.uk.akm.test.launchlistrx.data.db.LaunchDatabase
import co.uk.akm.test.launchlistrx.data.repo.impl.PersistentLaunchRepository
import co.uk.akm.test.launchlistrx.data.source.LaunchCache
import co.uk.akm.test.launchlistrx.data.source.LaunchDataSource
import co.uk.akm.test.launchlistrx.data.source.impl.DbLaunchCache
import co.uk.akm.test.launchlistrx.data.source.impl.RemoteLaunchDataSource
import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.interactor.impl.ListLaunchesUseCaseImpl
import co.uk.akm.test.launchlistrx.domain.repo.LaunchRepository
import co.uk.akm.test.launchlistrx.presentation.LaunchListMVP
import co.uk.akm.test.launchlistrx.presentation.presenter.LaunchListPresenter
import co.uk.akm.test.launchlistrx.util.retrofit.retrofitInstance
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit


val appModule = module {

    single<Gson> { GsonBuilder().setLenient().create() }

    single { retrofitInstance(BuildConfig.API_BASE_URL, get()) }

    single { (get() as Retrofit).create(LaunchService::class.java) }

    single { LaunchDatabase.singleInstance(androidApplication()).launchDao() }

    single<LaunchCache> { DbLaunchCache(BuildConfig.CACHE_EXPIRY_SECS, get()) }

    single<LaunchDataSource> { RemoteLaunchDataSource(get()) }

    single<LaunchRepository> { PersistentLaunchRepository(get(), get()) }

    single<ListLaunchesUseCase> { ListLaunchesUseCaseImpl(get()) }

    single<LaunchListMVP.Presenter> { LaunchListPresenter(get()) }
}
