package com.example.simplechat.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.simplechat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class SetProfileActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var selectedImageUri: Uri
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var imgPickPhoto: ImageView
    private lateinit var btnSelectPhoto: Button
    private lateinit var btnSendPhoto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profiel)

        // Initialize views
        mAuth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        imgPickPhoto = findViewById(R.id.imgPickPhoto)
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
        btnSendPhoto = findViewById(R.id.btnSendPhoto)

        // Set a click listener for the Select Photo button
        btnSelectPhoto.setOnClickListener {
            // Check and request storage permission
            checkAndRequestPermission()
        }

        // Set a click listener for the Send Photo button
        btnSendPhoto.setOnClickListener {
            // Handle the image upload logic here
            uploadProfilePicture()
        }

        // Initialize permission launcher
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with selecting and uploading the image
                openGallery()
            } else {
                // Permission denied, handle accordingly (e.g., show a message)
                checkAndRequestPermission()
                Toast.makeText(this@SetProfileActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Initialize content launcher
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // You have the selected image URI, now upload it to storage and Firestore
                selectedImageUri = uri
                uploadProfilePicture()
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, proceed with opening the gallery
            openGallery()
        } else {
            // Permission is not granted, request it
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun uploadProfilePicture() {
        // Implement the code to upload the image to Firebase Storage
        // After successful upload, get the download URL
        // Update the user's profile with the image URL in Firestore

        // Example: Upload the image to Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
        val imageStoragePath = "profilePictures/${mAuth.currentUser?.uid}.jpg"
        val imgReference = storageRef.child(imageStoragePath)

        val uploadTask = imgReference.putFile(selectedImageUri)
        imgPickPhoto.setImageURI(selectedImageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, get the download URL
            imgReference.downloadUrl.addOnSuccessListener { downloadUri ->
                // Now, 'downloadUri' contains the URL of the uploaded image

                // Update Firestore with the image download URL
                val userUid = mAuth.currentUser?.uid
                if (userUid != null) {
                    val userDocument = firestore.collection("profiles").document(userUid)

                    // Create a map to update the profilePictureDownloadUri field
                    val updateData = mapOf("profilePictureDownloadUri" to downloadUri.toString())

                    // Use set with merge option to create or update the document
                    userDocument.set(updateData, SetOptions.merge())
                        .addOnSuccessListener {
                            // Image URL updated in Firestore successfully
                            Toast.makeText(
                                this@SetProfileActivity,
                                "Image uploaded and URL updated",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Return to the previous activity (MainActivity) after uploading
                            val resultIntent = Intent()
                            resultIntent.putExtra("profileImageUri", selectedImageUri.toString())
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            // Handle Firestore update failure
                            Toast.makeText(
                                this@SetProfileActivity,
                                "Firestore update failed: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(this@SetProfileActivity, "User UID is null", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }.addOnFailureListener { exception ->
            // Handle image upload failure
            Toast.makeText(
                this@SetProfileActivity,
                "Image upload failed: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

//
//class SetProfileActivity : AppCompatActivity() {
//    private lateinit var permissionLauncher: ActivityResultLauncher<String>
//    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
//    private lateinit var getContent: ActivityResultLauncher<String>
//    private lateinit var selectedImageUri: Uri
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var imgPickPhoto: ImageView
//    private lateinit var btnSelectPhoto: Button
//    private lateinit var btnSendPhoto: Button
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_set_profiel)
//        // Initialize views
//        mAuth = FirebaseAuth.getInstance()
//        firestore = Firebase.firestore
//        imgPickPhoto = findViewById(R.id.imgPickPhoto)
//        btnSelectPhoto = findViewById(R.id.btnSelectPhoto)
//        btnSendPhoto = findViewById(R.id.btnSendPhoto)
//
//        // Set a click listener for the Select Photo button
//        btnSelectPhoto.setOnClickListener {
//            // Check and request storage permission
//            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
//        }
//
//        // Set a click listener for the Send Photo button
//        btnSendPhoto.setOnClickListener {
//            // Handle the image upload logic here
//            uploadProfilePicture()
//        }
//        requestPermissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            if (isGranted) {
//                // Permission granted, proceed with selecting and uploading the image
//                openGallery()
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message)
//                Toast.makeText(this@SetProfileActivity, "Permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//        permissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted ->
//            if (isGranted) {
//                // Permission granted, proceed with selecting and uploading the image
//                openGallery()
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message)
//                Toast.makeText(this@SetProfileActivity, "Permission denied", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//
//        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            if (uri != null) {
//                // You have the selected image URI, now upload it to storage and Firestore
//                selectedImageUri = uri
//                uploadProfilePicture()
//            }
//        }
//
//    }
//    private fun checkAndRequestPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            // Permission is already granted, proceed with opening the gallery
//            openGallery()
//        } else {
//            // Permission is not granted, request it
//            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//    }
//
////    private fun openGallery() {
////        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
////            if (uri != null) {
////                // You have the selected image URI, now upload it to storage and Firestore
////                selectedImageUri = uri
////                uploadProfilePicture()
////            }
////        }
////        getContent.launch("image/*")
////    }
//
//    private fun openGallery() {
//        getContent.launch("image/*")
//    }
//    private fun uploadProfilePicture() {
//        // Implement the code to upload the image to Firebase Storage
//        // After successful upload, get the download URL
//        // Update the user's profile with the image URL in Firestore
//
//        // Example: Upload the image to Firebase Storage
//        val storageRef = FirebaseStorage.getInstance().reference
//        val imageStoragePath = "profilePictures/${mAuth.currentUser?.uid}.jpg"
//        val imgReference = storageRef.child(imageStoragePath)
//
//        val uploadTask = imgReference.putFile(selectedImageUri)
//
//        uploadTask.addOnSuccessListener { taskSnapshot ->
//            // Image uploaded successfully, get the download URL
//            imgReference.downloadUrl.addOnSuccessListener { downloadUri ->
//                // Now, 'downloadUri' contains the URL of the uploaded image
//
//                // Update Firestore with the image download URL
//                val userUid = mAuth.currentUser?.uid
//                if (userUid != null) {
//                    val userDocument = firestore.collection("profiles").document(userUid)
//
//                    // Create a map to update the profilePictureDownloadUri field
//                    val updateData = mapOf("profilePictureDownloadUri" to downloadUri.toString())
//
//                    userDocument.update(updateData)
//                        .addOnSuccessListener {
//                            // Image URL updated in Firestore successfully
//                            Toast.makeText(
//                                this@SetProfileActivity,
//                                "Image uploaded and URL updated",
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                            // Return to the previous activity (MainActivity) after uploading
//                            val resultIntent = Intent()
//                            resultIntent.putExtra("profileImageUri", selectedImageUri.toString())
//                            setResult(Activity.RESULT_OK, resultIntent)
//                            finish()
//                        }
//                        .addOnFailureListener { exception ->
//                            // Handle Firestore update failure
//                            Toast.makeText(
//                                this@SetProfileActivity,
//                                "Firestore update failed: ${exception.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                } else {
//                    Toast.makeText(this@SetProfileActivity, "User UID is null", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }.addOnFailureListener { exception:Exception->
//            // Handle image upload failure
//            Toast.makeText(
//                this@SetProfileActivity,
//                "Image upload failed: ${exception.message}",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//        val resultIntent = Intent()
//        resultIntent.putExtra("profileImageUri", selectedImageUri.toString())
//        setResult(Activity.RESULT_OK, resultIntent)
//        finish()
//    }
//


    // Example of how to return to the previous activity (MainActivity) after uploading

//}


