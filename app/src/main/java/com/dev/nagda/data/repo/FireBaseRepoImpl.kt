package com.dev.nagda.data.repo

import com.dev.nagda.data.model.UserModel
import com.dev.nagda.domain.repo.FireBaseRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseRepoImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : FireBaseRepo {

    private val auth = FirebaseAuth.getInstance()
    private val usersCollection = firestore.collection("users")

    override suspend fun register(user: UserModel, password: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(
                "${user.phone}@nagda.com", password
            ).await()

            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))

            val userWithUid = user.copy(uid = uid)
            usersCollection.document(uid).set(userWithUid).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(phone: String, password: String): Result<UserModel> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(
                "${phone}@nagda.com", password
            ).await()

            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID is null"))

            val snapshot = usersCollection.document(uid).get().await()
            val user = snapshot.toObject(UserModel::class.java)
                ?: return Result.failure(Exception("User not found"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}