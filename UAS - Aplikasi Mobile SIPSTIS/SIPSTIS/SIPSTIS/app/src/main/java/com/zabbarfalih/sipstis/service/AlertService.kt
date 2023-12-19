package com.zabbarfalih.sipstis.service

import com.zabbarfalih.sipstis.model.Alert
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AlertService {
    @GET("/alert")
    suspend fun getAlerts(@Header("Authorization") token: String): List<Alert>

    @POST("/alert")
    suspend fun createAlert(@Header("Authorization") token: String, @Body ptype: Alert)

    @DELETE("/alert/{id}")
    suspend fun deleteAlert(@Header("Authorization") token: String, @Path("id") id: Long)
}