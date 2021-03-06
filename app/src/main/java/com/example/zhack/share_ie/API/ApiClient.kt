package com.example.zhack.share_ie.API

import com.example.zhack.share_ie.model.*
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiClient {

    @Headers(

            "Accept:application/json",
            "Content-Type: application/json",
            "Client-Service:frontend-client",
            "Auth-Key:simplerestapi"
    )
    @GET("berita")
    abstract fun getDetail(@Header("User-Id")UserId:String?,
                           @Header("Authorization")Auth:String?)
            : Call<status>

    @Headers(

            "Accept:application/json",
            "Content-Type: application/json",
            "Client-Service:frontend-client",
            "Auth-Key:simplerestapi"
    )
    @POST("berita/create")
    abstract fun create_status(@Header("User-Id")UserId:String?,
                               @Header("Authorization")Auth:String?,
                               @Body staus:status_created)
            : Call<status>


    @Headers(

            "Accept:application/json",
            "Content-Type: application/json",
            "Client-Service:frontend-client",
            "Auth-Key:simplerestapi"
    )
    @POST("auth/login")
    abstract fun getUser(@Query("username") UserName:String,
                         @Query("password") Password:String,
                         @Body body: Token_api):Call<User>

    @Headers(

            "Accept:application/json",
            "Content-Type: application/json",
            "Client-Service:frontend-client",
            "Auth-Key:simplerestapi"
    )
    @GET("users/detail/{id}")
    abstract fun getUserDetail(@Header("User-Id")UserId:String?,
                           @Header("Authorization")Auth:String?,
                               @Path("id")id:Int?)
            : Call<status_user_detail>

    companion object {
        val BASE_URL = "https://elennovation.com/khidir/index.php/"
        fun create(): ApiClient {
            val httpClient = OkHttpClient().newBuilder()
            val interceptor = Interceptor { chain ->
                val request = chain?.request()?.newBuilder()?.header("Client-Service","frontend-client")?.header("Auth-Key","simplerestapi")?.build()
                chain?.proceed(request)
            }
            httpClient.networkInterceptors().add(interceptor)
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofit.create(ApiClient::class.java)
        }
    }
}