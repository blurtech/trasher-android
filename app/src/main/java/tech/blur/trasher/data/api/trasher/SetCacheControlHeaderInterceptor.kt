package tech.blur.trasher.data.api.trasher

import okhttp3.Interceptor
import okhttp3.Response

class SetCacheControlHeaderInterceptor  : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val headerName = "Cache-Control"
        val request = chain.request()

        if (request.header(headerName) != null) {
            val offlineRequest = request.newBuilder()
                .header(headerName, "public, max-stale=" + request.header(headerName))
                .build()

            val response = chain.proceed(offlineRequest)
            if (response.isSuccessful) {
                return response
            }
        }

        return chain.proceed(request)
    }
}