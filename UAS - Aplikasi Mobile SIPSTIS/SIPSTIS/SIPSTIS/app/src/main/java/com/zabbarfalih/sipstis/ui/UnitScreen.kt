package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitScreen(
    unitViewModel: UnitViewModel,
    modifier: Modifier = Modifier,
    userState: UserState,
    navController: NavHostController,
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    showSpinner: () -> Unit = {}
) {
    unitViewModel.fetchPengajuans()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Button(
                onClick = { navController.navigate(SipstisScreen.CreatePengajuan.name) },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.tambah_pengajuan),
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(unitViewModel.pengajuans.value) { pengajuan ->
                PengajuanCard(pengajuan, navController)
            }
        }
    }
}

@Composable
fun PengajuanCard(
    pengajuan: Pengajuan,
    navController: NavHostController,
) {
    // Format Rupiah
    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    // Format Tanggal
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
    val tanggalFormatted = try {
        val tanggal = inputFormat.parse(pengajuan.tanggalPengadaan)
        outputFormat.format(tanggal)
    } catch (e: Exception) {
        pengajuan.tanggalPengadaan
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .height(215.dp)
            .padding(8.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                verticalArrangement = Arrangement.Center,
            ) {
                Row (
                ) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.wrapContentSize(),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = tanggalFormatted,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.wrapContentSize(),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "${pengajuan.status.toFormattedString()}",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pengajuan.namaPengadaan,
                    fontSize = 18.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W800
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${pengajuan.deskripsiPengadaan}",
                        fontSize =  12.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.wrapContentSize(),
                                color = MaterialTheme.colorScheme.tertiary
                            ) {
                                Text(
                                    text = "${pengajuan.estimasiHargaTotal?.let { formatRupiah.format(it.toDouble()) }}",
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W700
                                )
                            }
                        }

                        Spacer(modifier = Modifier.padding(2.dp))

                        Row (
                                modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        ) {
                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.wrapContentSize(),
                                color = MaterialTheme.colorScheme.onSecondary
                            ) {
                                Text(
                                    text = "${formatRupiah.format(pengajuan.estimasiHargaSatuan.toDouble())}/Unit",
                                    fontSize = 8.sp,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W500,
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            Surface(
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.wrapContentSize(),
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            {
                                Text(
                                    text = "${pengajuan.jumlah} Unit",
                                    fontSize = 8.sp,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.W500
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            navController.navigate("${SipstisScreen.DetailPengajuan.name}/${pengajuan.id}")
                        },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.detail),
                            fontSize = 11.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700
                        )
                    }
                }
            }
        }
    }
}

fun StatusEnum.toFormattedString(): String {
    return this.name
        .lowercase(Locale.getDefault())
        .split('_')
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
}