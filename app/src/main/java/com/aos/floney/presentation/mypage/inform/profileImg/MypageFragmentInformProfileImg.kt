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
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
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
            val imageFile = saveProfileImageToFile(requireContext()) // 프로필 이미지를 파일로 저장
            if (imageFile != null) {
                uploadImageToFirebaseStorage(imageFile) // Firebase Storage에 이미지 업로드
            } else {
                // 이미지 파일이 null인 경우 처리
                Toast.makeText(requireContext(), "Image file is null", Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.popBackStack()
        }
    }
    private fun uploadImageToFirebaseStorage(imageFile: Uri) {

        // arguments에서 email 값을 가져옴
        val email = arguments?.getString("email")
        Log.d("MypageFragment", "Received email: $email")

        if (!email.isNullOrEmpty()) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imagesRef = storageRef.child("dev/users/${email}")

            // 이미지가 없는 경우 그냥 업로드
            imagesRef.listAll().addOnSuccessListener { result ->
                if (result.items.isEmpty()) {
                    imagesRef.child(imageFile.lastPathSegment!!).putFile(imageFile).addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            val downloadUrl = uri.toString()
                            SampleToast.createToast(requireContext(), "Image uploaded successfully. Download URL: $downloadUrl")?.show()
                        }
                    }.addOnFailureListener { exception ->
                        SampleToast.createToast(requireContext(), "Image upload failed: ${exception.message} exception")?.show()
                    }
                } else {
                    // 이미지가 있는 경우 기존 이미지 삭제 후 업로드
                    result.items[0].delete().addOnSuccessListener {
                        imagesRef.child(imageFile.lastPathSegment!!).putFile(imageFile).addOnSuccessListener { taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUrl = uri.toString()
                                SampleToast.createToast(requireContext(), "Image uploaded successfully. Download URL: $downloadUrl")?.show()
                            }
                        }.addOnFailureListener { exception ->
                            SampleToast.createToast(requireContext(), "Image upload failed: ${exception.message} exception")?.show()
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                SampleToast.createToast(requireContext(), "Failed to check existing images: ${exception.message} exception")?.show()
            }
        }


    }
    private fun saveProfileImageToFile(context: Context): Uri? {
        val imageView = binding.profileImg

        // ImageView에서 Bitmap 가져오기
        imageView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(imageView.drawingCache)
        imageView.isDrawingCacheEnabled = false

        // Bitmap을 정사각형으로 자르기
        val squaredBitmap = cropBitmapToSquare(bitmap)

        // 정사각형 이미지를 파일로 저장하고 Uri 얻기
        return saveImageToFile(context, squaredBitmap)
    }

    private fun cropBitmapToSquare(bitmap: Bitmap): Bitmap {
        // 가장 짧은 쪽에 맞춰 정사각형으로 자르기
        val size = Math.min(bitmap.width, bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2

        return Bitmap.createBitmap(bitmap, x, y, size, size)
    }

    private fun saveImageToFile(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "profile_image.png")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return Uri.fromFile(file)
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