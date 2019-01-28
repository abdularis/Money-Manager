package com.aar.app.moneymanager.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Category(
        @Exclude val id: String,
        val name: String,
        val iconKey: String
)