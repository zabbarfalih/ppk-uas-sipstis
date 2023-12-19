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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
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
fun CreatePengajuanScreen(
    createPengajuanViewModel: CreatePengajuanViewModel,
    unitViewModel: UnitViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()

    // Format Rupiah
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
        currency = Currency.getInstance("IDR")
    }
    var estimasiHargaSatuan by remember { mutableStateOf(createPengajuanViewModel.estimasiHargaSatuanField) }
    var formattedEstimasiHargaSatuan by remember {
        mutableStateOf(formatRupiah.format(estimasiHargaSatuan.toDoubleOrNull() ?: 0.0))
    }
    fun handleEstimasiHargaSatuanChange(input: String) {
        val numericValue = input.filter { it.isDigit() }
        estimasiHargaSatuan = numericValue
        createPengajuanViewModel.updateEstimasiHargaSatuan(numericValue)
        // Format ulang nilai untuk tampilan
        formattedEstimasiHargaSatuan = formatRupiah.format(numericValue.toDoubleOrNull() ?: 0.0)
    }

    // Format Tanggal
    val displayFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
    val submitFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    var dateForSubmit by remember { mutableStateOf("") }
    var dateForDisplay by remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(year, monthOfYear, dayOfMonth)
            dateForSubmit = submitFormat.format(calendar.time)
            dateForDisplay = displayFormat.format(calendar.time)
            createPengajuanViewModel.updateTanggalPengadaan(dateForSubmit)
        }, year, month, day
    )

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        )
        {
            Text(
                text = stringResource(id = R.string.tambah_pengajuan),
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
                    modifier = modifier.padding(8.dp).fillMaxWidth()
                ) {
                    val textFieldModifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 0.dp)

                    OutlinedTextField(
                        value = createPengajuanViewModel.namaPengadaanField,
                        onValueChange = { createPengajuanViewModel.updateNamaPengadaan(it) },
                        label = { Text(
                            text = stringResource(id = R.string.nama_pengadaan),
                            fontSize = 13.sp
                        ) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = createPengajuanViewModel.deskripsiPengadaanField,
                        onValueChange = { createPengajuanViewModel.updateDeskripsiPengadaan(it) },
                        label = { Text(
                            text = stringResource(id = R.string.deskripsi_pengadaan),
                            fontSize = 13.sp
                        ) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = createPengajuanViewModel.jumlahField,
                        onValueChange = { createPengajuanViewModel.updateJumlah(it) },
                        label = { Text(
                            text = stringResource(id = R.string.jumlah),
                            fontSize = 13.sp
                        ) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = formattedEstimasiHargaSatuan,
                        onValueChange = { handleEstimasiHargaSatuanChange(it) },
                        label = { Text(
                            text = stringResource(id = R.string.estimasi_harga_satuan),
                            fontSize = 13.sp
                        ) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = dateForDisplay,
                        onValueChange = { },
                        label = { Text(
                            text = stringResource(id = R.string.tanggal_pengadaan),
                            fontSize = 13.sp
                        ) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        readOnly = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { datePickerDialog.show() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
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
                            showSpinner()
                            coroutineScope.launch {
                                val result = createPengajuanViewModel.createPengajuan()
                                when (result) {
                                    CreatePengajuanResult.Success -> {
                                        showMessage(R.string.sukses, R.string.pengajuan_dibuat)
                                        unitViewModel.fetchPengajuans()
                                        navController.popBackStack()
                                    }
                                    CreatePengajuanResult.BadInput -> showMessage(R.string.error, R.string.semua_field_harus_valid)
                                    else -> showMessage(R.string.error, R.string.network_error)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.submit),
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