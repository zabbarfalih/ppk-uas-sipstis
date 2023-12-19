package com.zabbarfalih.sipstis.ui

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
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
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanPPKScreen(
    ppkViewModel: PPKViewModel,
    navController: NavHostController,
    detailPengajuanPpkViewModel: DetailPengajuanPPKViewModel,
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val pengajuan by detailPengajuanPpkViewModel.pengajuan.collectAsState()
    val pengajuanId = navBackStackEntry.arguments?.getLong("pengajuanId") ?: 0L

    if (detailPengajuanPpkViewModel.showConfirmDialog1) {
        ConfirmDialog(
            onConfirmRequest = {
                detailPengajuanPpkViewModel.showConfirmDialog1 = false
                showSpinner()

                coroutineScope.launch {
                    when (detailPengajuanPpkViewModel.pembelianPengajuanPpk(pengajuanId)) {
                        KonfirmasiPPKResult1.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_pengajuan)
                            navController.navigate(SipstisScreen.Unit.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { detailPengajuanPpkViewModel.showConfirmDialog1 = false },
            message = R.string.konfirmasi_hapus_pengajuan
        )
    }

    if (detailPengajuanPpkViewModel.showConfirmDialog2) {
        ConfirmDialog(
            onConfirmRequest = {
                detailPengajuanPpkViewModel.showConfirmDialog2 = false
                showSpinner()

                coroutineScope.launch {
                    when (detailPengajuanPpkViewModel.selesaikanPengajuanPpk(pengajuanId)) {
                        KonfirmasiPPKResult2.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_pengajuan)
                            navController.navigate(SipstisScreen.Unit.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { detailPengajuanPpkViewModel.showConfirmDialog2 = false },
            message = R.string.konfirmasi_hapus_pengajuan
        )
    }

    LaunchedEffect(pengajuanId) {
        detailPengajuanPpkViewModel.getPengajuanById(pengajuanId)
        println("Pengajuan loaded: $pengajuan")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            SectionDetailPengajuanPPK(
                pengajuan = pengajuan,
                ppkViewModel = ppkViewModel,
                navController = navController,
                detailPengajuanPpkViewModel = detailPengajuanPpkViewModel,
                pengajuanId = pengajuanId,
                coroutineScope = coroutineScope,
                showSpinner = showSpinner,
                showMessage = showMessage,
            )
        }
    }
}

@Composable
fun SectionDetailPengajuanPPK(
    pengajuan: Pengajuan?,
    ppkViewModel: PPKViewModel,
    navController: NavHostController,
    detailPengajuanPpkViewModel: DetailPengajuanPPKViewModel,
    pengajuanId: Long,
    coroutineScope: CoroutineScope,
    showSpinner: () -> Unit,
    showMessage: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Text(
        text = stringResource(id = R.string.detail_pengajuan),
        style = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier.widthIn(0.dp, 240.dp)
    )

    Card (
        modifier = Modifier
            .padding(horizontal = 32.dp, vertical = 2.dp)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            DetailItemKonfirmasi(
                label = "Nama Pengadaan",
                value = pengajuan?.namaPengadaan ?: "Tidak tersedia",
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItemKonfirmasi(
                label = "Deskripsi Pengadaan",
                value = pengajuan?.deskripsiPengadaan ?: "Tidak tersedia",
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItemKonfirmasi(
                label = "Jumlah",
                value = "${pengajuan?.jumlah ?: 0}",
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItemKonfirmasi(
                label = "Estimasi Harga Satuan",
                value = formatRupiah.format(pengajuan?.estimasiHargaSatuan ?: 0.0),
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItemKonfirmasi(
                label = "Estimasi Harga Total",
                value = formatRupiah.format(pengajuan?.estimasiHargaTotal ?: 0.0),
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItemKonfirmasi(
                label = "Tanggal Pengadaan",
                value = pengajuan?.tanggalPengadaan?.let { convertDateToIndonesianFormat(it) } ?: "Tidak tersedia"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Row {
                    if (pengajuan?.status == StatusEnum.DIPROSES_PPK) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                showSpinner()
                                coroutineScope.launch {
                                    val result = pengajuan?.let {
                                        detailPengajuanPpkViewModel.pembelianPengajuanPpk(
                                            pengajuanId
                                        )
                                    }

                                    when (result) {
                                        KonfirmasiPPKResult1.Success -> {
                                            showMessage(R.string.sukses, R.string.proses_pengajuan)
                                            ppkViewModel.fetchPengajuans()
                                            navController.popBackStack()
                                        }

                                        else -> showMessage(R.string.error, R.string.network_error)
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Proses",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.W700,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (pengajuan?.status == StatusEnum.DIPROSES_PPK_PEMBELIAN) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                showSpinner()
                                coroutineScope.launch {
                                    val result = pengajuan?.let {
                                        Log.d(
                                            "DetailPengajuanKepalaBAUScreen",
                                            "Sending update request for pengajuan"
                                        )
                                        detailPengajuanPpkViewModel.selesaikanPengajuanPpk(
                                            pengajuanId
                                        )
                                    }

                                    when (result) {
                                        KonfirmasiPPKResult2.Success -> {
                                            showMessage(R.string.sukses, R.string.proses_selesai)
                                            ppkViewModel.fetchPengajuans()
                                            navController.popBackStack()
                                        }

                                        else -> showMessage(R.string.error, R.string.network_error)
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = "Selesaikan",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.W700,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
