package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zabbarfalih.sipstis.R
import kotlinx.coroutines.launch

@Composable
fun AlertManagementScreen(
    modifier: Modifier = Modifier,
    alertViewModel: AlertManagementViewModel = viewModel(factory = AlertManagementViewModel.Factory),
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    showSpinner: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }

    if (showConfirmDialog) {
        ConfirmDialog(
            onConfirmRequest = {
                showConfirmDialog = false
                showSpinner()
                scope.launch {
                    when (alertViewModel.deleteAlert()) {
                        DeleteAlertResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_jenis_masalah)
                            alertViewModel.getAlerts()
                        }
                        DeleteAlertResult.Error -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { showConfirmDialog = false },
            message = R.string.hapus_jenis_masalah
        )
    }

    AlertManagementScreenContent(
        alertUiState = alertViewModel.alertUiState,
        onDeleteClicked = { id ->
            alertViewModel.selectedId = id
            showConfirmDialog = true
        }
    )
}

@Composable
private fun AlertManagementScreenContent(
    alertUiState: AlertUiState,
    modifier: Modifier = Modifier,
    onDeleteClicked: (Long) -> Unit = {}
) {
    when(alertUiState) {
        is AlertUiState.Error -> {
            Text(text = "Error")
        }
        is AlertUiState.Loading -> {
            Text(text = "Loading")
        }
        is AlertUiState.Success -> {
            val alerts = alertUiState.alerts
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(items = alerts) { alert ->
                    ItemCard(
                        title = alert.name,
                        description = "",
                        options = {
                            Column{
                                DrawerNavigationItem(
                                    icons = Icons.Filled.Delete,
                                    text = R.string.hapus_jenis_masalah,
                                    onClick = { alert.id?.let { onDeleteClicked(it) } }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}