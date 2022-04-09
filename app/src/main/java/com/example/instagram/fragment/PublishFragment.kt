package com.example.instagram.fragment

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
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
import com.example.instagram.databinding.FragmentPublishBinding
import com.example.instagram.model.CurrentUser
import com.example.instagram.model.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


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

        binding.publishBtn.setOnClickListener {
            val date = Date()

            val filename = getFilename(date)
            uploadImageToStorage(filename, date)

        }

        val arr = arrayOf("Cali", "Medellín", "Bogotá")
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arr)
        binding.locationSpinner.adapter = arrayAdapter

        return view
    }

    private fun getFilename(date: Date): String {
        val user = CurrentUser.user
        return "${user?.username}_${date.time}"
    }

    private fun uploadImageToStorage(filename: String, date: Date){

        val storage = FirebaseStorage.getInstance("gs://apps-moviles-reto1.appspot.com")
        var storageRef = storage.reference

        val path = "images/${filename}.jpg"
        //var imagesRef: StorageReference? = storageRef.child("images")
        var imageRef = storageRef.child(path)

        val imageView = binding.image
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = imageRef.putBytes(data)

        Log.e(">>>", "Image upload started")

        uploadTask.addOnFailureListener {
            Log.e(">>>", "Failed image upload")
            Log.e(">>>", it.toString())
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.e(">>>", "Successfull image upload")
            createPost(path, date)

        }
    }

    private fun createPost(url: String, date: Date){
        val postId = UUID.randomUUID().toString()
        val user = CurrentUser.user
        val username = user?.username!!
        val caption = binding.captionTxt.text.toString()
        val location = binding.locationSpinner.selectedItem.toString()
        val post = Post(postId, username, date, url, caption, location)

        uploadPost(post)
    }

    private fun uploadPost(post: Post){
        Firebase.firestore
            .collection("post")
            .document(post.postId!!)
            .set(post)
            .addOnSuccessListener { successUploadPost() }
            .addOnFailureListener { e -> Log.e(">>>", "Error writing document", e) }
    }

    private fun successUploadPost(){
        Log.e(">>>", "DocumentSnapshot successfully written!")
        Toast.makeText(context, "Post Uploaded", Toast.LENGTH_SHORT).show()
    }

    private fun onCameraResult(result: ActivityResult){
        // Thumbnail
        //  val bitmap = result.data?.extras?.get("data") as Bitmap
        //  binding.image.setImageBitmap(bitmap)


        ///
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

            binding.image.setImageBitmap(bitmap)
        }else if (result.resultCode == RESULT_CANCELED){

        }
    }

    private fun onGalleryResult(result: ActivityResult) {
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