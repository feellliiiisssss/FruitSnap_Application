package com.dicoding.mybuah

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.mybuah.databinding.ActivityLoginBinding
import com.dicoding.mybuah.databinding.ActivityMainBinding
import com.dicoding.mybuah.response.LoginResponse
import com.dicoding.mybuah.response.UserData
import com.dicoding.mybuah.response.UserRegisResponse
import com.dicoding.mybuah.retrofit.ApiConfig
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
//    private lateinit var userdata : UserData
    private lateinit var loginData : LoginResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate((layoutInflater))
        setContentView(binding.root)

        binding.login.setOnClickListener{login()}

        binding.register.setOnClickListener{daftar()}
    }

    private fun login() {
        val username = binding.username.text.toString()
        val pass = binding.pass.text.toString()

        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", pass)
        }

        val client = ApiConfig.getApiService().getLoginToken(param)
        client.enqueue(object  : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        UserData(responseBody.token,username,pass)
//
//                        userdata.userToken = responseBody.token
//                        userdata.userName = username
//                        userdata.userPass = pass
//
//                        val userdata = UserData(
//                            loginData.token,
//                            username,
//                            pass
//                        )

                        val moveToMain = Intent(this@LoginActivity, MenuActivity::class.java)
                        moveToMain.putExtra(MenuActivity.USER_TOKEN,responseBody.token)
                        startActivity(moveToMain)
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Gagal, User Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()} gagal")

                    Toast.makeText(this@LoginActivity, "Login Gagal, User Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message} gagal")
            }
        })
    }

    private fun daftar() {
        val username = binding.username.text.toString()
        val pass = binding.pass.text.toString()

        val param = JsonObject().apply {
            addProperty("username", username)
            addProperty("password", pass)
        }

        val client = ApiConfig.getApiService().regisUser(param)
        client.enqueue(object  : retrofit2.Callback<UserRegisResponse> {
            override fun onResponse(
                call: Call<UserRegisResponse>,
                response: Response<UserRegisResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Toast.makeText(this@LoginActivity, "User Registered Successful, Please Login", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "User Already Registered, Use Another Information", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()} gagal")

                    Toast.makeText(this@LoginActivity, "User Already Registered, Use Another Information", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserRegisResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message} gagal")
            }
        })
    }
}