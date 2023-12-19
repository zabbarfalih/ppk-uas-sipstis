package com.zabbarfalih.sipstis.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanScreen(
    navController: NavHostController,
    detailPengajuanViewModel: DetailPengajuanViewModel,
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> }
) {
    val pengajuan by detailPengajuanViewModel.pengajuan.collectAsState()

    val pengajuanId = navBackStackEntry.arguments?.getLong("pengajuanId") ?: 0L

    LaunchedEffect(pengajuanId) {
        detailPengajuanViewModel.getPengajuanById(pengajuanId)
    }

    // Format Rupiah dan Tanggal
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))

    // Fungsi untuk mengonversi tanggal
    fun formatDate(input: String?): String {
        return if (input != null) {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(input)
                outputFormat.format(date)
            } catch (e: Exception) {
                input
            }
        } else {
            "Tidak tersedia"
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.padding(12.dp))

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detail Pengajuan",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                DetailItem(
                    label = "Nama Pengadaan:",
                    value = pengajuan?.namaPengadaan ?: "Tidak tersedia"
                )
                DetailItem(
                    label = "Tanggal Pengadaan:",
                    value = formatDate(pengajuan?.tanggalPengadaan)
                )
                DetailItem(
                    label = "Deskripsi Pengadaan:",
                    value = pengajuan?.deskripsiPengadaan ?: "Tidak tersedia"
                )
                DetailItem(label = "Jumlah:", value = "${pengajuan?.jumlah ?: 0}")
                DetailItem(
                    label = "Estimasi Harga Satuan:",
                    value = formatRupiah.format(pengajuan?.estimasiHargaSatuan ?: 0.0)
                )
                DetailItem(
                    label = "Status:",
                    value = pengajuan?.status?.toFormattedString() ?: "Tidak tersedia"
                )

                Spacer(modifier = Modifier.height(16.dp))

                val isRevisiDisabled = pengajuan?.status != StatusEnum.REVISI
                Button(
                    onClick = { /* Perform action if revisi is clicked and is not disabled */ },
                    enabled = !isRevisiDisabled
                ) {
                    Text(
                        text = stringResource(id = R.string.revisi_pengajuan),
                        fontWeight = FontWeight.W700
                    )
                }
            }

            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hapus Pengajuan",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    val isDeleteDisabled = pengajuan?.status != StatusEnum.SELESAI && pengajuan?.status != StatusEnum.DIPROSES_PPK_PEMBELIAN && pengajuan?.status != StatusEnum.DIPROSES_PBJ_PEMBELIAN && pengajuan?.status != StatusEnum.DIPROSES_PBJ && pengajuan?.status != StatusEnum.DIPROSES_PPK && pengajuan?.status != StatusEnum.REVISI
                    Button(
                        onClick = { detailPengajuanViewModel.deletePengajuan(pengajuanId) },
                        enabled = !isDeleteDisabled
                    ) {
                        Text(
                            text = stringResource(id = R.string.hapus_pengajuan),
                            fontWeight = FontWeight.W700
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label ",
            fontWeight = FontWeight.W700,
            fontSize = 11.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.W500,
            fontSize = 11.sp
        )
    }
}
