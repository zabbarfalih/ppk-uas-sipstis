package com.zabbarfalih.sipstis.ui

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
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
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.model.isDeletable
import com.zabbarfalih.sipstis.model.isRevisable
import com.zabbarfalih.sipstis.model.toColor
import com.zabbarfalih.sipstis.model.toTextColor
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.CoroutineScope
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
    unitViewModel: UnitViewModel,
    navController: NavHostController,
    detailPengajuanViewModel: DetailPengajuanViewModel,
    modifier: Modifier = Modifier,
    navBackStackEntry: NavBackStackEntry,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()
    val pengajuan by detailPengajuanViewModel.pengajuan.collectAsState()
    val pengajuanId = navBackStackEntry.arguments?.getLong("pengajuanId") ?: 0L

    val isRevisiDisabled = pengajuan?.status?.isRevisable() != true
    val isDeleteDisabled = pengajuan?.status?.isDeletable() != true

    // Format Tanggal
    val displayFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
    val submitFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var dateForSubmit by remember { mutableStateOf("") }
    var dateForDisplay by remember { mutableStateOf("") }


    // Format Rupiah
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
        currency = Currency.getInstance("IDR")
    }
    var estimasiHargaSatuan by remember { mutableStateOf(detailPengajuanViewModel.estimasiHargaSatuanField) }
    var formattedEstimasiHargaSatuan by remember {
        mutableStateOf(formatRupiah.format(estimasiHargaSatuan.toDoubleOrNull() ?: 0.0))
    }
    fun handleEstimasiHargaSatuanChange(input: String) {
        val numericValue = input.filter { it.isDigit() }
        estimasiHargaSatuan = numericValue
        detailPengajuanViewModel.updateEstimasiHargaSatuan(numericValue)
        formattedEstimasiHargaSatuan = formatRupiah.format(numericValue.toDoubleOrNull() ?: 0.0)
    }

    LaunchedEffect(pengajuan) {
        pengajuan?.estimasiHargaSatuan?.let {
            estimasiHargaSatuan = it.toString()
            formattedEstimasiHargaSatuan = formatRupiah.format(it)
        }
        pengajuan?.tanggalPengadaan?.let {
            val initialDate = submitFormat.parse(it)
            initialDate?.let { date ->
                dateForSubmit = it
                dateForDisplay = displayFormat.format(date)
            }
        }
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(year, monthOfYear, dayOfMonth)
            dateForSubmit = submitFormat.format(calendar.time)
            dateForDisplay = displayFormat.format(calendar.time)
            detailPengajuanViewModel.updateTanggalPengadaan(dateForSubmit)
        }, year, month, day
    )

    if (detailPengajuanViewModel.showConfirmDialog) {
        ConfirmDialog(
            onConfirmRequest = {
                detailPengajuanViewModel.showConfirmDialog = false
                showSpinner()

                coroutineScope.launch {
                    when (detailPengajuanViewModel.deletePengajuan(pengajuanId)) {
                        DeletePengajuanResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_pengajuan)
                            navController.navigate(SipstisScreen.Unit.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { detailPengajuanViewModel.showConfirmDialog = false },
            message = R.string.konfirmasi_hapus_pengajuan
        )
    }

    LaunchedEffect(pengajuanId) {
        detailPengajuanViewModel.getPengajuanById(pengajuanId)
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

            SectionDetailPengajuan(
                pengajuan = pengajuan,
                unitViewModel = unitViewModel,
                navController = navController,
                isRevisiDisabled = isRevisiDisabled,
                detailPengajuanViewModel = detailPengajuanViewModel,
                pengajuanId = pengajuanId,
                coroutineScope = coroutineScope,
                showSpinner = showSpinner,
                showMessage = showMessage,
                datePickerDialog = datePickerDialog,
                dateForSubmit = dateForSubmit,
                dateForDisplay = dateForDisplay,
                formattedEstimasiHargaSatuan = formattedEstimasiHargaSatuan,
                handleEstimasiHargaSatuanChange = { handleEstimasiHargaSatuanChange(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

//            if (!isDeleteDisabled) {
            SectionHapusPengajuan(
                detailPengajuanViewModel = detailPengajuanViewModel,
                isDeleteDisabled = isDeleteDisabled
            )
//            }
        }
    }
}

@Composable
fun SectionDetailPengajuan(
    pengajuan: Pengajuan?,
    unitViewModel: UnitViewModel,
    navController: NavHostController,
    isRevisiDisabled: Boolean,
    detailPengajuanViewModel: DetailPengajuanViewModel,
    pengajuanId: Long,
    coroutineScope: CoroutineScope,
    showSpinner: () -> Unit,
    showMessage: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    datePickerDialog: DatePickerDialog,
    dateForSubmit: String,
    dateForDisplay: String,
    formattedEstimasiHargaSatuan: String,
    handleEstimasiHargaSatuanChange: (String) -> Unit
) {
    val scope = rememberCoroutineScope()

    Text(
        text = if (isRevisiDisabled) stringResource(id = R.string.detail_pengajuan) else stringResource(
            id = R.string.revisi_pengajuan
        ),
        style = TextStyle(
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.W700,
            fontSize = 20.sp,
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
            val textFieldModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 0.dp)

            DetailItem(
                label = "Nama Pengadaan",
                value = pengajuan?.namaPengadaan ?: "Tidak tersedia",
                onValueChange = { detailPengajuanViewModel.updateNamaPengadaan(it) },
                isReadOnly = true,
                enabled = false,
                keyboard = KeyboardType.Text,
                ime = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItem(
                label = "Deskripsi Pengadaan",
                value = pengajuan?.deskripsiPengadaan ?: "Tidak tersedia",
                onValueChange = { detailPengajuanViewModel.updateDeskripsiPengadaan(it) },
                isReadOnly = isRevisiDisabled,
                enabled = !isRevisiDisabled,
                keyboard = KeyboardType.Text,
                ime = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItem(
                label = "Jumlah",
                value = "${pengajuan?.jumlah ?: 0}",
                onValueChange = { detailPengajuanViewModel.updateJumlah(it) },
                isReadOnly = isRevisiDisabled,
                enabled = !isRevisiDisabled,
                keyboard = KeyboardType.Number,
                ime = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItem(
                label = "Estimasi Harga Satuan",
                value = formattedEstimasiHargaSatuan,
                onValueChange = { handleEstimasiHargaSatuanChange(it) },
                isReadOnly = isRevisiDisabled,
                enabled = !isRevisiDisabled,
                keyboard = KeyboardType.Number,
                ime = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(4.dp))

            DetailItem(
                label = "Tanggal Pengadaan",
                value = dateForDisplay,
                onValueChange = { detailPengajuanViewModel.updateTanggalPengadaan(it) },
                isReadOnly = true,
                enabled = !isRevisiDisabled,
                keyboard = KeyboardType.Number,
                ime = ImeAction.Done
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { datePickerDialog.show() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                enabled = !isRevisiDisabled
            ) {
                Text(
                    text = stringResource(id = R.string.pilih_tanggal),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W500,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    Log.d("DetailPengajuanScreen", "Update button clicked")
                    showSpinner()
                    coroutineScope.launch {
                        val updatedPengajuan =
                            detailPengajuanViewModel.estimasiHargaSatuanField.toDoubleOrNull()?.let {
                                pengajuan?.copy(
                                    deskripsiPengadaan = detailPengajuanViewModel.deskripsiPengadaanField,
                                    jumlah = detailPengajuanViewModel.jumlahField.toIntOrNull() ?: 0,
                                    estimasiHargaSatuan = it,
                                    tanggalPengadaan = detailPengajuanViewModel.tanggalPengadaanField,
                                    status = StatusEnum.MENUNGGU_PERSETUJUAN
                                )
                            }
                        if (updatedPengajuan != null) {
                            val result = detailPengajuanViewModel.updatePengajuan(pengajuanId, updatedPengajuan)
                            when (result) {
                                DetailPengajuanResult.Success -> {
                                    showMessage(R.string.sukses, R.string.berhasil_revisi_pengajuan)
                                    unitViewModel.fetchPengajuans()
                                    navController.popBackStack()
                                }
                                DetailPengajuanResult.BadInput -> showMessage(R.string.error, R.string.semua_field_harus_valid)
                                else -> showMessage(R.string.error, R.string.network_error)
                            }

                        } else {
                            showMessage(R.string.error, R.string.semua_field_harus_valid)
                        }
                    }
                },
                enabled = !isRevisiDisabled
            ) {
                Text(
                    text = "Ubah",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun SectionHapusPengajuan(
    detailPengajuanViewModel: DetailPengajuanViewModel,
    modifier: Modifier = Modifier,
    isDeleteDisabled: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(
            text = stringResource(id = R.string.hapus_pengajuan),
            style = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
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
                Button(
                    onClick = { detailPengajuanViewModel.showConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !isDeleteDisabled
                ) {
                    Text(
                        text = stringResource(id = R.string.hapus_pengajuan),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.W700,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DetailItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isReadOnly: Boolean,
    enabled: Boolean,
    keyboard: KeyboardType,
    ime: ImeAction,
) {
    var text by rememberSaveable { mutableStateOf(value) }
    LaunchedEffect(value) {
        text = value
    }

    val keyboardOptions = if (!isReadOnly) {
        KeyboardOptions(
            keyboardType = keyboard,
            imeAction = ime
        )
    } else {
        KeyboardOptions(
        )
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            if (!isReadOnly) {
                onValueChange(it)
            }
        },
        label = { Text(text = label, fontFamily = poppinsFontFamily) },
        singleLine = true,
        modifier = textFieldModifier,
        readOnly = isReadOnly,
        enabled = enabled,
        keyboardOptions = keyboardOptions
    )
}


enum class UpdatePengajuanResult {
    Success,
    Error,
    EmptyField
}

enum class DeletePengajuanResult {
    Success,
    Error
}