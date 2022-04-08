package com.example.instagram.model

import com.google.firebase.firestore.PropertyName

data class User (
    var id: String? = "",
    var username: String? = "",
    var email: String? = "",
    var password: String? = "",
    var profilePhotoURL: String? = ""
    )