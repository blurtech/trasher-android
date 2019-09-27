package tech.blur.trasher.data.api.trasher

import android.net.ConnectivityManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory(
    gsonBuilder: GsonBuilder,
    scheduler: Scheduler,
    private val connectivityManager: ConnectivityManager
) : CallAdapter.Factory() {
    private val original = RxJava2CallAdapterFactory.createWithScheduler(scheduler)
    private val gson = gsonBuilder.create()

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        val wrapped = original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>

        return RxCallAdapterWrapper(wrapped, gson, connectivityManager)
    }

    private class RxCallAdapterWrapper<R>(val wrappedCallAdapter: CallAdapter<R, *>,
                                          val gson: Gson,
                                          val connectivityManager: ConnectivityManager
    ) : CallAdapter<R, Any> {
        override fun responseType(): Type = wrappedCallAdapter.responseType()

        override fun adapt(call: Call<R>): Any {
            val result = wrappedCallAdapter.adapt(call)

            return when (result) {
                is Single<*> -> result.onErrorResumeNext { e -> Single.error(e) }
                is Completable -> result.onErrorResumeNext { e -> Completable.error(e) }
                else -> throw IllegalStateException("Only Single and Completable can be used")
            }
        }
    }
}