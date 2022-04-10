package com.example.instagram.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.instagram.state.SharedPref
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.model.User
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
            SharedPref.logOut()
            activity?.finish()
        }

        binding.publishBtn.setOnClickListener {
            // TODO: Make user update method in SharedPref with persistence
            val username = binding.name.text.toString()
            val profilePath = this.file?.path
            val user = User("", username, "", "", profilePath)
            SharedPref.updateProfile(user)
            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
        }

        val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onCameraResult)
        binding.changProfilePhotoTxt.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val url = SharedPref.currentUser?.profilePhotoURL
            if (url.equals("")){
                val filename = UUID.randomUUID().toString()
                this.file = File("${context?.getExternalFilesDir(null)}/${filename}.png")
            }else{
                this.file = File(url)
            }

            //this.file = File("${context?.getExternalFilesDir(null)}/profilePhoto.png")
            // Log.e(">>> P", file?.path.toString())
            val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, this.file!!)
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
           binding.profilePhoto.setImageBitmap(scaledBitmap)
        }else if (result.resultCode == Activity.RESULT_CANCELED){

        }
    }



    private fun populateFields(){

        val user = SharedPref.currentUser

        if (user != null){
            binding.name.setText(user.username)

            if (!user.profilePhotoURL.equals("")){
                val bitmap = BitmapFactory.decodeFile(user.profilePhotoURL)
                binding.profilePhoto.setImageBitmap(bitmap)
            }else{
                // TODO: Place default image
            }

        }
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