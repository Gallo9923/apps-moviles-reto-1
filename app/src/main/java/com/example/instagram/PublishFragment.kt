package com.example.instagram

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import com.example.instagram.databinding.FragmentPublishBinding
import java.io.File

class PublishFragment : Fragment() {

    private var _binding: FragmentPublishBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        val view = binding.root

        val cameraLauncher = registerForActivityResult(StartActivityForResult(), ::onCameraResult)
        val galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        binding.cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File("${context?.getExternalFilesDir(null)}/photo.png")
            Log.e(">>>", file?.path.toString())
            val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(intent)
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

        // Inflate the layout for this fragment
        return view
    }

    fun onCameraResult(result: ActivityResult){
        // Thumbnail
        //  val bitmap = result.data?.extras?.get("data") as Bitmap
        //  binding.image.setImageBitmap(bitmap)
        if(result.resultCode == RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(file?.path)
            // TODO: Set a better width and height
            val thumbnail = Bitmap.createScaledBitmap(bitmap, bitmap.width/4, bitmap.height/4, true)
            binding.image.setImageBitmap(thumbnail)
        }else if (result.resultCode == RESULT_CANCELED){

        }
    }

    fun onGalleryResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK){
            val uriImage = result.data?.data
            uriImage?.let {
                 binding.image.setImageURI(uriImage)
            }
        }else if (result.resultCode == RESULT_CANCELED){

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PublishFragment()
    }
}