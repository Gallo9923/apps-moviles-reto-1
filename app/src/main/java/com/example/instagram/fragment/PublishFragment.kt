package com.example.instagram.fragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.instagram.UtilDomi
import com.example.instagram.databinding.FragmentPublishBinding
import com.example.instagram.model.Post
import com.example.instagram.state.SharedPref
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class PublishFragment : Fragment() {

    private var _binding: FragmentPublishBinding? = null
    private val binding get() = _binding!!

    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        val view = binding.root

        val cameraLauncher = registerForActivityResult(StartActivityForResult(), ::onCameraResult)
        val galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        binding.cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val filename = UUID.randomUUID().toString()
            this.file = File("${context?.getExternalFilesDir(null)}/${filename}.png")
            // Log.e(">>>", file?.path.toString())
            val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            cameraLauncher.launch(intent)
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            galleryLauncher.launch(intent)
        }

        binding.publishBtn.setOnClickListener {
            createPost()
            Toast.makeText(context, "Photo published", Toast.LENGTH_SHORT).show()
        }

        val arr = arrayOf("Cali", "Medellín", "Bogotá")
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, arr)
        binding.locationSpinner.adapter = arrayAdapter

        return view
    }

    private fun createPost(){
        val postId = UUID.randomUUID().toString()
        val user = SharedPref.currentUser
        val username = user?.username!!
        val caption = binding.captionTxt.text.toString()
        val location = binding.locationSpinner.selectedItem.toString()
        val post = Post(postId, username, Date(), this.file!!.path, caption, location)
        // Log.e("PublishFragment", "${this.file?.path}")
        SharedPref.addPost(post)
    }

    private fun onCameraResult(result: ActivityResult){
        if(result.resultCode == RESULT_OK){
            val bitmap = BitmapFactory.decodeFile(file?.path)
            // TODO: Set a better width and height
            val aspectRatio = (bitmap.width.toFloat())/bitmap.height
            val scaledBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (aspectRatio*300).toInt(),
                300,
                true
            )

            //Log.e("Aspect Ratio", "height: ${bitmap.height} width: ${bitmap.width} aspect ratio:${aspectRatio}")
//            val matrix = Matrix()
//            matrix.postRotate(90f)

            binding.image.setImageBitmap(scaledBitmap)
        }else if (result.resultCode == RESULT_CANCELED){

        }
    }

    private fun onGalleryResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK){
            // TODO: URI to Bitmap and then resize image
            val uriImage = result.data?.data
            uriImage?.let {
                 binding.image.setImageURI(uriImage)
            }
            val originPath = UtilDomi.getPath(requireContext(), uriImage!!)


            val filename = UUID.randomUUID().toString()
            this.file = File("${context?.getExternalFilesDir(null)}/${filename}.png")

            copy(File(originPath), this.file)

//            Log.e(">>>", "Gallery: ${uriImage.path}")
//            Log.e(">>>", "Gallery: ${this.file!!.path}")

        }else if (result.resultCode == RESULT_CANCELED){

        }
    }

    @Throws(IOException::class)
    fun copy(src: File?, dst: File?) {
        FileInputStream(src).use { `in` ->
            FileOutputStream(dst).use { out ->
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            }
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