package com.example.instagram

import com.google.firebase.firestore.PropertyName

data class User (
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var password: String? = null
    )