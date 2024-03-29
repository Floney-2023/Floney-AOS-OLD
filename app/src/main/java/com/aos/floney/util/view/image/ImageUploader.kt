// ImageUploader.kt

import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

object ImageUploader {

    interface UploadListener {
        fun onUploadSuccess(downloadUrl: String)
        fun onUploadFailure(errorMessage: String)
    }

    fun uploadProfileImage(context: Context, email: String?, imageFile: Uri, listener: UploadListener) {
        if (!email.isNullOrEmpty()) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imagesRef = storageRef.child("dev/users/$email")

            imagesRef.listAll().addOnSuccessListener { result ->
                if (result.items.isEmpty()) {
                    uploadImage(imagesRef.child(imageFile.lastPathSegment!!), imageFile, context, listener)
                } else {
                    result.items[0].delete().addOnSuccessListener {
                        uploadImage(imagesRef.child(imageFile.lastPathSegment!!), imageFile, context, listener)
                    }
                }
            }.addOnFailureListener { exception ->
                listener.onUploadFailure("Failed to check existing images: ${exception.message} exception")
            }
        }
    }

    private fun uploadImage(storageRef: StorageReference, imageFile: Uri, context: Context, listener: UploadListener) {
        storageRef.putFile(imageFile).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                listener.onUploadSuccess(downloadUrl)
            }
        }.addOnFailureListener { exception ->
            listener.onUploadFailure("Image upload failed: ${exception.message} exception")
        }
    }
}
