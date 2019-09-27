package tech.blur.hacktemplate.di

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

var networkModule = module {
//    single {
//        Retrofit.Builder()
//            .baseUrl("")
//            .client(
//                OkHttpClient.Builder()
//                    .cache(Cache(androidApplication().cacheDir, 10 * 1024 * 1024)) // 10MB
//                    .addInterceptor(SetCacheControlHeaderInterceptor())
//                    .addInterceptor(SetContentTypeHeaderToJsonInterceptor())
//                    .addInterceptor(
//                        SetAuthorizationHeaderInterceptor(
//                            get(),
//                            SetAuthorizationHeaderMode.IF_REQUIRED
//                        )
//                    )
//                    .addInterceptor(SetApiVersionHeaderInterceptor())
//                    .addInterceptor(SetClientBuildNumberHeaderInterceptor())
//                    .addInterceptor(SetDeviceInfoHeaderInterceptor())
//                    .addInterceptor(SetDeveloperNameInterceptor(appConfigProvider.developerName))
//                    .addInterceptor(SetTimeoutsHeaderInterceptor())
//                    .addInterceptor(SetDevModeEnabledHeaderInterceptor(get()))
//                    //.addInterceptor(SetSkillXClientHeaderInterceptor())
//                    .apply {
//                        if (appConfigProvider.isDebug) {
//                            addInterceptor(
//                                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//                            )
//                        }
//                    }
//                    .readTimeout(10, TimeUnit.SECONDS)
//                    .writeTimeout(10, TimeUnit.SECONDS)
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .build()
//            )
//            .addConverterFactory(GsonConverterFactory.create(
//                (get() as GsonBuilder).create()
//            ))
//            .addCallAdapterFactory(
//                RxErrorHandlingCallAdapterFactory(
//                    get(),
//                    get<SchedulerProvider>().io(),
//                    androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//                )
//            )
//            .build()
//            .create(SkillApi::class.java)
//    }
//
//    single {
//        Retrofit.Builder()
//            .baseUrl(appConfigProvider.skillAdminApiBaseUrl)
//            .client(
//                OkHttpClient.Builder()
//                    .addInterceptor(SetContentTypeHeaderToJsonInterceptor())
//                    .readTimeout(10, TimeUnit.SECONDS)
//                    .writeTimeout(10, TimeUnit.SECONDS)
//                    .connectTimeout(10, TimeUnit.SECONDS)
//                    .build()
//            )
//            .addConverterFactory(GsonConverterFactory.create((get() as GsonBuilder).create()))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//            .create(SkillAdminApi::class.java)
//    }
}
