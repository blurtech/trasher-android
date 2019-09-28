package tech.blur.trasher.di

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.blur.trasher.common.rx.AppSchedulerProvider
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.data.AccountRepository
import tech.blur.trasher.data.TrashRepository
import tech.blur.trasher.presentation.MainActivity
import tech.blur.trasher.presentation.MainActivityViewModel
import tech.blur.trasher.presentation.auth.LoginViewModel
import tech.blur.trasher.presentation.auth.RegistrationViewModel
import tech.blur.trasher.presentation.map.MapViewModel

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

    viewModel { MainActivityViewModel(get()) }
    viewModel { MapViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegistrationViewModel(get(), get(), get()) }

}

private val rxModule = module {
    single { AppSchedulerProvider() as SchedulerProvider }
}

val blurApp = listOf(appModule, rxModule)