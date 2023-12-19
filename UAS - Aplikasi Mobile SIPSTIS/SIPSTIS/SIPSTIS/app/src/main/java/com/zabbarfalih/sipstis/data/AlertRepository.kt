package com.zabbarfalih.sipstis.data

import com.zabbarfalih.sipstis.model.Alert
import com.zabbarfalih.sipstis.service.AlertService

interface AlertRepository {
    suspend fun getAlerts(token: String): List<Alert>
    suspend fun createAlert(token: String, ptype: Alert)
    suspend fun deleteAlert(token: String, id: Long)
}

class NetworkAlertRepository(private val alertService: AlertService) : AlertRepository {
    override suspend fun getAlerts(token: String): List<Alert> = alertService.getAlerts("Bearer $token")
    override suspend fun createAlert(token: String, ptype: Alert) = alertService.createAlert("Bearer $token", ptype)
    override suspend fun deleteAlert(token: String, id: Long) = alertService.deleteAlert("Bearer $token", id)
}