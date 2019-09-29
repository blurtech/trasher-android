package tech.blur.trasher.di

import android.content.Context
import com.google.maps.GeoApiContext
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.blur.trasher.UserSession
import tech.blur.trasher.common.rx.AppSchedulerProvider
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.data.TrashRepository
import tech.blur.trasher.presentation.MainActivityViewModel
import tech.blur.trasher.presentation.auth.LoginViewModel
import tech.blur.trasher.presentation.auth.RegistrationViewModel
import tech.blur.trasher.presentation.map.MapViewModel
import tech.blur.trasher.presentation.profile.UserProfileViewModel
import tech.blur.trasher.presentation.qr.QRScanerViewModel
import tech.blur.trasher.presentation.trashEjection.TrashEjectionViewModel

private var appModule = module {

    single {
        AccountRepository(
            androidApplication().getSharedPreferences(
                "Credentials",
                Context.MODE_PRIVATE
            )
        )
    }

    single {
        TrashRepository(
            androidApplication().getSharedPreferences(
                "Trash",
                Context.MODE_PRIVATE
            )
        )
    }

    single {
        GeoApiContext.Builder()
            .apiKey("AIzaSyDVsJx-Hyq6w4laps9vUcA1gbq-mWLtH78")
            .build()
    }

    single { UserSession() }

    viewModel { TrashEjectionViewModel(get(), get(), get()) }
    viewModel { UserProfileViewModel(get(), get(), get()) }
    viewModel { QRScanerViewModel(get(), get()) }
    viewModel { MainActivityViewModel(get()) }
    viewModel { MapViewModel(get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }

}

private val rxModule = module {
    single { AppSchedulerProvider() as SchedulerProvider }
}

val blurApp = listOf(appModule, rxModule)