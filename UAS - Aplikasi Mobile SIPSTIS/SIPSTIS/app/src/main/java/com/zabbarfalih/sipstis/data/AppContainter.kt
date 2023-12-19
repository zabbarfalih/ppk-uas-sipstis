package com.zabbarfalih.sipstis.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zabbarfalih.sipstis.service.PengajuanService
import com.zabbarfalih.sipstis.service.UserService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val userRepository: UserRepository
    val pengajuanRepository: PengajuanRepository
}

class DefaultAppContainer() : AppContainer {
    private val baseUrl = "http://192.168.1.18:8080/"

    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory(contentType))
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build())
        .build()

    private val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    private val pengajuanService: PengajuanService by lazy {
        retrofit.create(PengajuanService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(userService)
    }

    override val pengajuanRepository: PengajuanRepository by lazy {
        NetworkPengajuanRepository(pengajuanService)
    }
}