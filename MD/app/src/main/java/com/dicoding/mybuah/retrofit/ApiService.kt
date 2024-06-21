package com.dicoding.mybuah.retrofit

import com.dicoding.mybuah.response.FileUploadResponse
import com.dicoding.mybuah.response.LoginResponse
import com.dicoding.mybuah.response.UserRegisResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @POST("login?")
    fun getLoginToken(
        @Body raw: JsonObject
    ): Call<LoginResponse>

    @POST("register?")
    fun regisUser(
        @Body raw: JsonObject
    ): Call<UserRegisResponse>

    @Multipart
    @POST("predict")
    suspend fun uploadImage(
        @Header("Authorization") authHeader :String?,
        @Part file: MultipartBody.Part
    ): FileUploadResponse
}