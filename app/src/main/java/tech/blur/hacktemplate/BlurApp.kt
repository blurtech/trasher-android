package tech.blur.hacktemplate

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tech.blur.hacktemplate.di.blurApp
import tech.blur.hacktemplate.di.networkModule

class BlurApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BlurApp)

            modules(blurApp.union(listOf(networkModule)).toList())
        }
    }
}
