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
                ?: return Result.failure(Exception("المستخدم غير موجود"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<UserModel> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            val user = usersCollection.document(uid).get().await()
                .toObject(UserModel::class.java)
                ?: return Result.failure(Exception("المستخدم غير موجود"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(user: UserModel): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("المستخدم غير مسجل الدخول"))

            usersCollection.document(uid).update(
                mapOf(
                    "fullName"   to user.fullName,
                    "phone"      to user.phone,
                    "address"    to user.address,
                    "mail"       to user.mail,
                    "familySize" to user.familySize,
                    "notes"      to user.notes
                )
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}