package tech.blur.trasher.data.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import tech.blur.trasher.domain.LoginRequest
import tech.blur.trasher.domain.LoginResponse
import tech.blur.trasher.domain.RegisterRequest
import tech.blur.trasher.domain.Wrapper

interface TrasherApi {

    @POST("user")
    fun login(@Body loginRequest: LoginRequest): Single<Wrapper<LoginResponse>>

    @POST("user/register")
    fun register(@Body registerRequest: RegisterRequest): Single<Wrapper<LoginResponse>>

}