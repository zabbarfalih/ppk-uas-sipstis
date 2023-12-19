package com.zabbarfalih.sipstis.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPengajuanKepalaBAUScreen(
    kepalaBauViewModel: KepalaBAUViewModel,
    navController: NavHostController,
    detailPengajuanKepalaBauViewModel: DetailPengajuanKepalaBAUViewModel,
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val pengajuan by detailPengajuanKepalaBauViewModel.pengajuan.collectAsState()
    val pengajuanId = navBackStackEntry.arguments?.getLong("pengajuanId") ?: 0L

    if (detailPengajuanKepalaBauViewModel.showConfirmDialog1) {
        ConfirmDialog(
            onConfirmRequest = {
                detailPengajuanKepalaBauViewModel.showConfirmDialog1 = false
                showSpinner()

                coroutineScope.launch {
                    when (detailPengajuanKepalaBauViewModel.setujuiPengajuan(pengajuanId)) {
                        KonfirmasiKepalaBAUResult1.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_pengajuan)
                            navController.navigate(SipstisScreen.Unit.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { detailPengajuanKepalaBauViewModel.showConfirmDialog1 = false },
            message = R.string.konfirmasi_hapus_pengajuan
        )
    }

    if (detailPengajuanKepalaBauViewModel.showConfirmDialog2) {
        ConfirmDialog(
            onConfirmRequest = {
                detailPengajuanKepalaBauViewModel.showConfirmDialog2 = false
                showSpinner()

                coroutineScope.launch {
                    when (detailPengajuanKepalaBauViewModel.tolakPengajuan(pengajuanId)) {
                        KonfirmasiKepalaBAUResult2.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_pengajuan)
                            navController.navigate(SipstisScreen.Unit.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { detailPengajuanKepalaBauViewModel.showConfirmDialog2 = false },
            message = R.string.konfirmasi_hapus_pengajuan
        )
    }

    if (detailPengajuanKepalaBauViewModel.showConfirmDialog3) {
        ConfirmDialog(
            onConfirmRequest = {
                detailPengajuanKepalaBauViewModel.showConfirmDialog3 = false
                showSpinner()

                coroutineScope.launch {
                    when (detailPengajuanKepalaBauViewModel.revisiPengajuan(pengajuanId)) {
                        KonfirmasiKepalaBAUResult3.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_pengajuan)
                            navController.navigate(SipstisScreen.Unit.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { detailPengajuanKepalaBauViewModel.showConfirmDialog3 = false },
            message = R.string.konfirmasi_hapus_pengajuan
        )
    }


    LaunchedEffect(pengajuanId) {
        detailPengajuanKepalaBauViewModel.getPengajuanById(pengajuanId)
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

            SectionDetailPengajuanKepalaBAU(
                pengajuan = pengajuan,
                kepalaBauViewModel = kepalaBauViewModel,
                navController = navController,
                detailPengajuanKepalaBauViewModel = detailPengajuanKepalaBauViewModel,
                pengajuanId = pengajuanId,
                coroutineScope = coroutineScope,
                showSpinner = showSpinner,
                showMessage = showMessage,
                modifier = modifier
            )
        }
    }
}

@Composable
fun SectionDetailPengajuanKepalaBAU(
    pengajuan: Pengajuan?,
    kepalaBauViewModel: KepalaBAUViewModel,
    navController: NavHostController,
    detailPengajuanKepalaBauViewModel: DetailPengajuanKepalaBAUViewModel,
    pengajuanId: Long,
    coroutineScope: CoroutineScope,
    showSpinner: () -> Unit,
    showMessage: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Text(
        text = stringResource(id = R.string.konfirmasi_pengajuan),
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
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            showSpinner()
                            coroutineScope.launch {
                                val result = pengajuan?.let {
                                    detailPengajuanKepalaBauViewModel.setujuiPengajuan(
                                        pengajuanId
                                    )
                                }

                                when (result) {
                                    KonfirmasiKepalaBAUResult1.Success -> {
                                        showMessage(R.string.sukses, R.string.berhasil_terima_pengajuan)
                                        kepalaBauViewModel.fetchPengajuans()
                                        navController.popBackStack()
                                    }
                                    else -> showMessage(R.string.error, R.string.network_error)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Setujui",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Yellow,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            showSpinner()
                            coroutineScope.launch {
                                val result = pengajuan?.let {
                                    Log.d("DetailPengajuanKepalaBAUScreen", "Sending update request for pengajuan")
                                    detailPengajuanKepalaBauViewModel.revisiPengajuan(
                                        pengajuanId
                                    )
                                }

                                when (result) {
                                    KonfirmasiKepalaBAUResult3.Success -> {
                                        showMessage(R.string.sukses, R.string.berhasil_tolak_revisi_pengajuan)
                                        kepalaBauViewModel.fetchPengajuans()
                                        navController.popBackStack()
                                    }
                                    else -> showMessage(R.string.error, R.string.network_error)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Revisi",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            showSpinner()
                            coroutineScope.launch {
                                val result = pengajuan?.let {
                                    detailPengajuanKepalaBauViewModel.tolakPengajuan(
                                        pengajuanId
                                    )
                                }

                                when (result) {
                                    KonfirmasiKepalaBAUResult2.Success -> {
                                        showMessage(R.string.sukses, R.string.berhasil_tolak_pengajuan)
                                        kepalaBauViewModel.fetchPengajuans()
                                        navController.popBackStack()
                                    }
                                    else -> showMessage(R.string.error, R.string.network_error)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Tolak",
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

@Composable
fun DetailItemKonfirmasi(
    label: String,
    value: String,
) {
    var text by rememberSaveable { mutableStateOf(value) }
    LaunchedEffect(value) {
        text = value
    }

        OutlinedTextField(
        value = text,
        onValueChange = {},
        label = { Text(text = label, fontFamily = poppinsFontFamily) },
        singleLine = true,
        modifier = textFieldModifier,
        readOnly = true,
    )
}