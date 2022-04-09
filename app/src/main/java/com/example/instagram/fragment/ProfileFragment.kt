package com.example.instagram.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.load.Key
import com.example.instagram.GlideApp
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.model.CurrentUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null


    @SuppressLint("FragmentLiveDataObserve")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.logOutBtn.setOnClickListener {
            CurrentUser.logOut()
            activity?.finish()

        }

        binding.publishBtn.setOnClickListener {

        }

        val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onCameraResult)
        binding.changProfilePhotoTxt.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${context?.getExternalFilesDir(null)}/profilePhoto.png")
            Log.e(">>>", file?.path.toString())
            val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(intent)
        }

        populateFields()

        return view
    }

    override fun onResume() {
        super.onResume()
        populateFields()
    }

    private fun onCameraResult(result: ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(file?.path)
            // TODO: Set a better width and height
            val aspectRatio = (bitmap.width.toFloat())/bitmap.height
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (aspectRatio*300).toInt(),
                300,
                true
            )
            uploadImageToStorage(scaledBitmap)
        }else if (result.resultCode == Activity.RESULT_CANCELED){

        }
    }

    private fun uploadImageToStorage(bitmap: Bitmap){

        val userId = CurrentUser.user?.id

        val storage = FirebaseStorage.getInstance("gs://apps-moviles-reto1.appspot.com")
        var storageRef = storage.reference
        val path = "profiles/${userId}.jpg"
        var imageRef = storageRef.child(path)

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = imageRef.putBytes(data)

        Log.e(">>>", "Image upload started")

        uploadTask.addOnFailureListener {
            Log.e(">>>", "Failed profile image upload")
            Log.e(">>>", it.toString())
        }.addOnSuccessListener {
            Log.e(">>>", "Successfull profile image upload")
            updateProfilePhoto(path, bitmap)
        }
    }

    private fun updateProfilePhoto(path: String, bitmap: Bitmap){

        val userId = CurrentUser.user?.id
        Firebase.firestore
            .collection("users")
            .document(userId!!)
            .update("profilePhotoURL", path)
            .addOnSuccessListener { successProfileUpdate(bitmap)}
            .addOnFailureListener { e -> Log.e(">>>", "Error writing document", e) }
    }

    private fun successProfileUpdate(bitmap: Bitmap){
        Toast.makeText(context, "Successfull profile image upload", Toast.LENGTH_SHORT).show()
        binding.profilePhoto.setImageBitmap(bitmap)
    }

    private fun populateFields(){
        val currentName = CurrentUser.user?.username
        binding.name.setText(currentName)

        // Profile photo
        val fullURL = "gs://apps-moviles-reto1.appspot.com/${CurrentUser.user?.profilePhotoURL}"
        val storage = FirebaseStorage.getInstance()
        val gsReference = storage.getReferenceFromUrl(fullURL)
        binding.profilePhoto.invalidate()
        GlideApp.with(requireContext())
            .load(gsReference)
            .signature(Key { "Size" })
            .into(binding.profilePhoto)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}