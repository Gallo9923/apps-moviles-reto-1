package com.example.instagram.state

import android.content.SharedPreferences
import android.util.Log
import com.example.instagram.model.Post
import com.example.instagram.model.User
import com.google.gson.Gson

object  SharedPref {

    // Shared preferences keys
    private val USERS = "users"
    private val CURRENT_USER = "currentUser"
    private val POSTS = "posts"

    // Shared preferences
    var sharedPref: SharedPreferences? = null

    // Users
    private val users = ArrayList<User>()

    // Current User
    var currentUser: User? = null

    // Posts

    val posts = ArrayList<Post>()

    // Logging Logics

    fun logIn(user: User){
        this.currentUser = user

        val json = Gson().toJson(user)
        this.sharedPref?.edit()?.putString(this.CURRENT_USER, json)?.apply()
    }

    fun logOut(){
        val json = Gson().toJson(null)
        this.sharedPref?.edit()?.putString(this.CURRENT_USER, json)?.apply()
    }

    fun loadPreviousUser(){
        val json = sharedPref?.getString(this.CURRENT_USER, "NO_DATA")
        if(json != null && json != "NO_DATA" && json != "null"){
            val currentUser: User = Gson().fromJson(json, User::class.java)
            this.currentUser = currentUser
        }else{
            this.currentUser = null
        }
    }

    // Users Logic

    fun findUserById(id: String): User? {
        var userToFound: User? = null

        for(user: User in users){
            if (user.id.equals(id)){
                userToFound = user
                break
            }
        }

        return userToFound
    }

    fun findUserByUsername(username: String): User? {

        var userToFound: User? = null

        for(user: User in users){
            if (user.username.equals(username)){
                userToFound = user
            }
        }

        return userToFound
    }

    fun loadUsers(){
        val json = this.sharedPref?.getString(this.USERS, "NO_DATA")

        if (json != "NO_DATA"){
            val userArray: Array<User> = Gson().fromJson(json, Array<User>::class.java)
            this.users.addAll(userArray)
        }else {
            val userArray = initUsers()
            this.users.addAll(userArray)
        }
    }

    private fun initUsers(): Array<User>{

        val startUsers = arrayOf(
            User("1111", "alfa", "alfa@gmail.com", "aplicacionesmoviles", ""),
            User("2222", "beta", "beta@gmail.com", "aplicacionesmoviles", ""),
            User("3333", "1", "chris@gmail.com", "1", ""),
        )
        val json = Gson().toJson(startUsers)
        this.sharedPref?.edit()?.putString(this.USERS, json)?.apply()

        return startUsers
    }

    // Post Logic

    fun loadPosts(){
        val json = this.sharedPref?.getString(this.POSTS, "NO_DATA")
        if (json != "NO_DATA"){
            val userArray: Array<Post> = Gson().fromJson(json, Array<Post>::class.java)
            this.posts.addAll(userArray)
        }
    }

    fun addPost(post: Post){
        this.posts.add(post)
        this.posts.sortByDescending { it.date }


        val json = Gson().toJson(this.posts)
        this.sharedPref?.edit()?.putString(this.POSTS, json)?.apply()
    }


    // Photo Logic

    fun updateProfile(user: User){

        // Update in memory
        this.currentUser?.profilePhotoURL = user.profilePhotoURL
        this.currentUser?.username = user.username

        for(currUser: User in users){
            if (currUser.id == currentUser?.id){
                currUser.username == user.username
                currUser.profilePhotoURL == user.profilePhotoURL
                break
            }
        }

        //Save with persistence
        val usersJson =Gson().toJson(this.users)
        this.sharedPref?.edit()?.putString(this.USERS, usersJson)?.apply()

        val currentUserjson = Gson().toJson(this.currentUser)
        this.sharedPref?.edit()?.putString(this.CURRENT_USER, currentUserjson )?.apply()
    }

}