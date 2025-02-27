package com.vaibhav.taskify.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.vaibhav.taskify.data.local.dataSource.DataStorePreferencesDataSource
import com.vaibhav.taskify.data.local.dataSource.PreferencesDataSource
import com.vaibhav.taskify.data.local.dataSource.SharedPreferencesDataSource
import com.vaibhav.taskify.data.local.room.TaskDAO
import com.vaibhav.taskify.data.local.room.TaskifyDatabase
import com.vaibhav.taskify.data.models.mappper.TaskMapper
import com.vaibhav.taskify.data.models.mappper.UserMapper
import com.vaibhav.taskify.data.remote.harperDb.Api
import com.vaibhav.taskify.util.BASE_URL
import com.vaibhav.taskify.util.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {


    @Provides
    @Singleton
    fun providesFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun providesSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("TaskifySharedPref", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun providesFirebaseStorage() = Firebase.storage.reference


    @Provides
    @Singleton
    @Named("sharedPref")
    fun providesSharedPreferencesDataSource(sharedPreferences: SharedPreferences): PreferencesDataSource =
        SharedPreferencesDataSource(sharedPreferences)

    @Provides
    @Singleton
    @Named("prefDataStore")
    fun providesDataStorePreferencesDataSource(dataStore: DataStore<Preferences>): PreferencesDataSource =
        DataStorePreferencesDataSource(dataStore)

    @Provides
    @Singleton
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit) = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun providesUserMapper(): UserMapper = UserMapper()

    @Provides
    @Singleton
    fun providesTaskMapper(): TaskMapper = TaskMapper()


    @Provides
    @Singleton
    fun providesTaskifyDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, TaskifyDatabase::class.java, "TaskifyDatabase"
    )
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    @Singleton
    fun providesTaskDao(taskifyDatabase: TaskifyDatabase): TaskDAO = taskifyDatabase.getTaskDao()


    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context


}