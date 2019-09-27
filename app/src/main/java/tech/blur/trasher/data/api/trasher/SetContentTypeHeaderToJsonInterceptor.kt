package tech.blur.trasher.data.api.trasher

import okhttp3.Interceptor
import okhttp3.Response

class SetContentTypeHeaderToJsonInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val jsonMediaType = "application/json"
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Content-Type", jsonMediaType)
                .addHeader("Accept", jsonMediaType)
                .build()
        )
    }
}