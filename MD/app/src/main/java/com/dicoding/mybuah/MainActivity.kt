package com.dicoding.mybuah

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.dicoding.mybuah.CameraActivity.Companion.CAMERAX_RESULT
import com.dicoding.mybuah.databinding.ActivityMainBinding
import com.dicoding.mybuah.response.FileUploadResponse
import com.dicoding.mybuah.response.LoginResponse
import com.dicoding.mybuah.response.returnToken
import com.dicoding.mybuah.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private lateinit var bitmap: Bitmap


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate((layoutInflater))
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        binding.camera.setOnClickListener {startCameraX()}

        binding.galery.setOnClickListener {startGallery()}

        binding.identifikasi.setOnClickListener {uploadImage()}

    }

    private fun startCameraX() {

        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)

    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    @SuppressLint("SuspiciousIndentation")
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image Classification File", "showImage: ${imageFile.path}")
//            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                try {
                    val token  = intent.getStringExtra(USER_TOKEN)
                    val apiService = ApiConfig.getApiService()
                    val successResponse = apiService.uploadImage(token, multipartBody)

                        if (successResponse.message == "Image has already been predicted") {
                            binding.hasil.text = (successResponse.predicted)
                        } else {
                            binding.hasil.text = ("Gambar gagal diprediksi")
                        }

//                    showLoading(false)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
//                    showLoading(false)
                }
            }


        } ?: Toast.makeText(this, "gambarnya gaada", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val USER_TOKEN = "user_token"
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imagePreview.setImageURI(it)
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,currentImageUri)

        }
    }


}