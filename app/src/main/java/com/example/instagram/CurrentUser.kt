package com.example.instagram

object CurrentUser {

    var user: User? = null

    fun logOut(){
        user = null
    }

}