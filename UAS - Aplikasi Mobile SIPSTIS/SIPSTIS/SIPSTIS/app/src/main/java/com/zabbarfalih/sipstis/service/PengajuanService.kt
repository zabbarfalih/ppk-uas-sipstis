package com.zabbarfalih.sipstis.service

import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.Pengajuan
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PengajuanService {
    @POST("/unit/pengajuan")
    suspend fun createPengajuan(@Header("Authorization") token: String, @Body pengajuan: PengajuanForm)

    @GET("/unit/pengajuan")
    suspend fun getAllPengajuan(@Header("Authorization") token: String): List<Pengajuan>

    @GET("/unit/pengajuan/{pengajuanId}")
    suspend fun getPengajuanById(@Header("Authorization") token: String, @Path("pengajuanId") pengajuanId: Long): Pengajuan

    @DELETE("/unit/pengajuan/{pengajuanId}")
    suspend fun deletePengajuan(@Header("Authorization") token: String, @Path("reportId") reportId: Long)

    @PATCH("/unit/pengajuan/{pengajuanId}")
    suspend fun updatePengajuan(@Header("Authorization") token: String, @Path("reportId") reportId: Long, @Body pengajuan: Pengajuan)
}