package tech.blur.trasher.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.blur.trasher.common.rx.SchedulerProvider
import tech.blur.trasher.data.api.TrasherApi
import tech.blur.trasher.data.api.trasher.RxErrorHandlingCallAdapterFactory
import tech.blur.trasher.data.api.trasher.SetAuthorizationHeaderInterceptor
import tech.blur.trasher.data.api.trasher.SetContentTypeHeaderToJsonInterceptor
import java.util.concurrent.TimeUnit

var networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://dev-trasher-backend.herokuapp.com/")
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
                    .addInterceptor(SetContentTypeHeaderToJsonInterceptor())
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                 GsonBuilder().create()
            ))
            .addCallAdapterFactory(
                RxErrorHandlingCallAdapterFactory(
                    GsonBuilder(),
                    get<SchedulerProvider>().io(),
                    androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                )
            )
            .build()
            .create(TrasherApi::class.java)
    }
}
