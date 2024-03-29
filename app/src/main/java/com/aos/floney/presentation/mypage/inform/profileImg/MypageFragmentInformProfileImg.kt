package com.aos.floney.presentation.mypage.inform.profileImg

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageInformProfilechangeBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.util.view.SampleToast
import com.aos.floney.util.view.image.ImageToUri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class MypageFragmentInformProfileImg : BindingFragment<FragmentMypageInformProfilechangeBinding>(R.layout.fragment_mypage_inform_profilechange) {
    private val viewModel: MypageViewModel by viewModels()
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetting()
    }

    private fun initSetting() {

        val email = arguments?.getString("email")
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.profileView.setOnClickListener {
            // 이미지 선택
            showImagePickerOptions()
        }
        binding.profileChangeBasic.setOnClickListener {
            binding.profileImg.setImageResource(R.drawable.btn_profile)
        }
        binding.changeButton.setOnClickListener {
            // 이미지 변경 요청
            val imageFile = ImageToUri.saveProfileImageToFile(requireContext(), binding.profileImg) // 프로필 이미지를 파일로 저장
            if (imageFile != null) {
                ImageUploader.uploadProfileImage(requireContext(), email, imageFile, object : ImageUploader.UploadListener {
                    override fun onUploadSuccess(downloadUrl: String) {
                        // 업로드 성공 시 처리
                        SampleToast.createToast(requireContext(), "Image uploaded successfully. Download URL: $downloadUrl")?.show()
                        Timber.d("Image uploaded successfully. Download URL: $downloadUrl")

                    }

                    override fun onUploadFailure(errorMessage: String) {
                        // 업로드 실패 시 처리
                        SampleToast.createToast(requireContext(), errorMessage)?.show()
                    }
                })
            } else {
                // 이미지 파일이 null인 경우 처리
                Toast.makeText(requireContext(), "Image file is null", Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.popBackStack()
        }
    }
    private fun showImagePickerOptions() {
        val items = arrayOf(getString(R.string.camera), getString(R.string.gallery),getString(R.string.random_image))

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.choose_image_source))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> dispatchPickImageIntent()
                    2 -> dispatchRamdomPictureIntent()
                }
            }
            .show()
    }

    private fun dispatchTakePictureIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun dispatchPickImageIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    private fun dispatchRamdomPictureIntent() {
        val randomImageResourceId = getRandomDrawableResourceId()
        val randomImageDrawable = ContextCompat.getDrawable(requireContext(), randomImageResourceId)
        binding.profileImg.setImageDrawable(randomImageDrawable)
    }
    private fun getRandomDrawableResourceId(): Int {
        val drawableArray = arrayOf(
            R.drawable.img___01,
            R.drawable.img___02,
            R.drawable.img___03
        )
        val randomIndex = (drawableArray.indices).random()
        return drawableArray[randomIndex]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.profileImg.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri = data?.data
                    binding.profileImg.setImageURI(selectedImageUri)
                }
            }
        }
    }
}