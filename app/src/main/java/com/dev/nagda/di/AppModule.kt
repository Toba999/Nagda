package com.dev.nagda.di

import android.content.Context
import com.dev.nagda.data.geofence.GeoFenceChecker
import com.dev.nagda.data.repo.FireBaseRepoImpl
import com.dev.nagda.domain.repo.FireBaseRepo
import com.dev.nagda.utils.LocationPermissionChecker
import com.dev.nagda.utils.SharedPrefManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPrefManager(@ApplicationContext context: Context): SharedPrefManager =
        SharedPrefManager(context)

    @Provides
    @Singleton
    fun provideFireBaseRepo(
        firestore: FirebaseFirestore,
    ): FireBaseRepo {
        return FireBaseRepoImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideLocationPermissionChecker(@ApplicationContext context: Context): LocationPermissionChecker {
        return LocationPermissionChecker(context)
    }

    @Provides
    @Singleton
    fun provideGeoFenceChecker(): GeoFenceChecker {
        return GeoFenceChecker()
    }

}