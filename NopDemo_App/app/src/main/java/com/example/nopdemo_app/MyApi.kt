package com.example.nopdemo_app

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MyApi {

    @Multipart
    @POST("/upload")
    fun upload(
        @Part file: MultipartBody.Part
    ):Call<UploadResponse>


}