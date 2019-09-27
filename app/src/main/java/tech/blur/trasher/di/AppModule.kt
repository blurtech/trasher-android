package tech.blur.trasher.di

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import tech.blur.trasher.data.AccountRepository

private var appModule = module{

    single { AccountRepository(androidApplication().getSharedPreferences("DevMode", Context.MODE_PRIVATE)) }

}

val blurApp = listOf(appModule)