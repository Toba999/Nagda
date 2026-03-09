package com.dev.nagda.data.repo

import android.content.Context
import com.dev.nagda.domain.repo.FireBaseRepo
import com.dev.nagda.utils.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FireBaseRepoImpl @Inject constructor(
    firestore: FirebaseFirestore,
    storage: FirebaseStorage,
    private val sharedPrefManager: SharedPrefManager,
    @ApplicationContext private val context: Context
) : FireBaseRepo {



}