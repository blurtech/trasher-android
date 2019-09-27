package tech.blur.trasher.data.api.trasher

import okhttp3.Interceptor
import okhttp3.Response
import tech.blur.trasher.data.AccountRepository

class SetAuthorizationHeaderInterceptor(
    private val accountRepository: AccountRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val headerName = "Authorization"
        val request = chain.request()

        if (request.headers().get(headerName) == null) {
            return chain.proceed(request)
        }

        check(accountRepository.isUserLoggedIn) { "Access token info is not exists" }

        val token = accountRepository.userToken
        return chain.proceed(
            request.newBuilder()
                .header(headerName, "${token.type} ${token.token}")
                .build()
        )
    }
}