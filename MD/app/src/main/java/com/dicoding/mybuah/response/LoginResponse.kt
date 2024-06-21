package com.dicoding.mybuah.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("token")
	val token: String
)

fun LoginResponse.returnToken(): LoginResponse{
	return LoginResponse(token)
}
