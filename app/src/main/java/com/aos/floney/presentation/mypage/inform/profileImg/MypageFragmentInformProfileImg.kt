package com.aos.floney.presentation.mypage.inform.profileImg

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
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
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import java.io.File
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
            binding.profileImg
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
    /*private fun showImagePickerOptions() {

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val randomImageIntent = Intent("com.example.ACTION_PICK_RANDOM_IMAGE")

        val chooser = Intent.createChooser(Intent(), "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(galleryIntent, cameraIntent, randomImageIntent))

        //startActivity(chooser)
        val component = chooser.resolveActivity(requireActivity().packageManager)
        if (component != null) {
            Log.d("profilechange", "component: $component")

            // Check if the selected option is the camera or gallery
            if (component?.packageName == cameraIntent.resolveActivity(requireActivity().packageManager)?.packageName) {
                // The user chose the camera option
                startActivityForResult(chooser, GET_CAMERA_IMAGE)
            } else {
                // The user chose the gallery option
                startActivityForResult(chooser, GET_GALLERY_IMAGE)
            }
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
    }*/
}