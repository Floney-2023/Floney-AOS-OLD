package com.aos.floney.presentation.mypage.inform.profileImg

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageInformProfilechangeBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class MypageFragmentInformProfileImg : BindingFragment<FragmentMypageInformProfilechangeBinding>(R.layout.fragment_mypage_inform_profilechange) {
    private val viewModel: MypageViewModel by viewModels()
    private val GET_GALLERY_IMAGE = 1
    private val GET_CAMERA_IMAGE = 2
    private var capturedImageUri: Uri? = null
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSetting()
    }

    private fun initSetting() {
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
            parentFragmentManager.popBackStack()
        }
    }

    private fun showImagePickerOptions() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"


        val photoFile: File? = createImageFile()

        if (photoFile != null) {
            capturedImageUri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.android.fileprovider",
                photoFile
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
        }

        val chooser = Intent.createChooser(Intent(), "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(galleryIntent, cameraIntent))

        if (chooser.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(chooser, GET_GALLERY_IMAGE)
        } else {
            // Handle the case where there is no app available to handle the intent
            Toast.makeText(requireContext(), "No app available to handle the intent", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_GALLERY_IMAGE -> {
                    val selectedImageUri = data?.data
                    binding.profileImg.setImageURI(selectedImageUri)
                }
                GET_CAMERA_IMAGE -> {
                    // Use capturedImageUri to load the image into ImageView
                    binding.profileImg.setImageURI(capturedImageUri)
                }
            }
        }
    }
}