package com.example.instagram.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagram.adapter.PostAdapter
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.model.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapater: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        layoutManager = LinearLayoutManager(context)
        binding.postRecycler.layoutManager = layoutManager
        binding.postRecycler.setHasFixedSize(true)

        adapater = PostAdapter()
        adapater.setContext(requireContext()) // TODO: Check better way

        binding.postRecycler.adapter = adapater

        return view
    }

    override fun onResume() {
        super.onResume()
        adapater.notifyDataSetChanged()
    }

//    private fun readPost(){
//
//        val db = Firebase.firestore
//        db.collection("post")
//            .orderBy("date", Query.Direction.DESCENDING)
//            .get()
//            .addOnSuccessListener { documents ->
//
//                var posts = ArrayList<Post>()
//
//                for (document in documents) {
//                    Log.e(">>>", "${document.id} => ${document.data}")
//                    var post = document.toObject(Post::class.java)
//                    posts.add(post)
//                }
//
//                adapater.setPosts(posts)
//            }
//            .addOnFailureListener { exception ->
//                Log.w(">>>", "Error getting documents: ", exception)
//            }
//
//
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}