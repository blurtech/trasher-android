package tech.blur.trasher

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tech.blur.trasher.di.blurApp
import tech.blur.trasher.di.networkModule

class BlurApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BlurApp)

            modules(blurApp.union(listOf(networkModule)).toList())
        }
    }
}
