package tech.blur.trasher.data.api

import io.reactivex.Single
import retrofit2.http.*
import tech.blur.trasher.domain.*

interface TrasherApi {

    @POST("user")
    fun login(@Body loginRequest: LoginRequest): Single<Wrapper<LoginResponse>>

    @POST("user/register")
    fun register(@Body registerRequest: RegisterRequest): Single<Wrapper<LoginResponse>>

    @GET("litterstorage/")
    @Headers("Authorization: required")
    fun getTrashCans(): Single<Wrapper<List<Trashcan>>>

    @GET("litterstorage/")
    @Headers("Authorization: required")
    fun getTrashCans(@Query("city") city: String): Single<Wrapper<List<Trashcan>>>

    @POST("litter/")
    @Headers("Authorization: required")
    fun ejectTrash(@Body ejectTrashRequest: EjectTrashRequest): Single<Wrapper<Trash>>

    @POST("qr/scan")
    @Headers("Authorization: required")
    fun registeParcels(@Body parcel: Parcel): Single<Wrapper<Int>>

    @GET("user")
    @Headers("Authorization: required")
    fun getProfile(): Single<Wrapper<User>>

    @GET("game/mapreduce")
    @Headers("Authorization: required")
    fun getStatistic(): Single<Wrapper<ArrayList<Statistic>>>
}