package com.dicoding.mybuah.response

import com.google.gson.annotations.SerializedName

data class UserRegisResponse(

	@field:SerializedName("message")
	val message: String
)
