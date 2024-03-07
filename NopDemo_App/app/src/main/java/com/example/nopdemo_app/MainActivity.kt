package com.example.nopdemo_app

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class MainActivity : AppCompatActivity() {
    private val requestToFile = 1
    private var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uploadBtn = findViewById<Button>(R.id.uploadBtn)
        uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, requestToFile)
            Toast.makeText(this, "Select a Specific Document", Toast.LENGTH_SHORT).show()
        }


    }

    private fun UploadFile(file: File) {
        val requestFile = file.asRequestBody("*/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        myapi.upload(body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    if ((uploadResponse != null) && (uploadResponse.success)) {
                        Toast.makeText(
                            this@MainActivity,
                            "File uploaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "File Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "File upload failed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
//                val textview= findViewById<TextView>(R.id.tv)
//                textview.text="File upload failed: ${t.message}"
                t.printStackTrace()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == requestToFile && resultCode == RESULT_OK) {
            val fileUri = intent?.data
            if (fileUri != null) {
                val textview= findViewById<TextView>(R.id.tv)
                textview.text = fileUri.toString()

                UploadFile(File(fileUri.path!!))

            }
        }
    }

    val retrofit = Retrofit.Builder()
        .baseUrl(" https://v2.convertapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val myapi = retrofit.create(MyApi::class.java)


}




