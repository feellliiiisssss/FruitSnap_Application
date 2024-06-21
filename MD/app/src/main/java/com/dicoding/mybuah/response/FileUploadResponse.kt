package com.dicoding.mybuah.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("predicted")
	val predicted: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)
