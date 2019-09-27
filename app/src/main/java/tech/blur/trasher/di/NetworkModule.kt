package tech.blur.trasher.di

import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.data.api.trasher.SetAuthorizationHeaderInterceptor
import java.util.concurrent.TimeUnit

var networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://dev-trasher-frontend.herokuapp.com/")
            .client(
                OkHttpClient.Builder()
                    .cache(Cache(androidApplication().cacheDir, 10 * 1024 * 1024)) // 10MB
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(
                        SetAuthorizationHeaderInterceptor(
                            get()
                        )
                    )
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                (get() as GsonBuilder).create()
            ))
            .build()
            .create(TrasherApi::class.java)
    }
}
