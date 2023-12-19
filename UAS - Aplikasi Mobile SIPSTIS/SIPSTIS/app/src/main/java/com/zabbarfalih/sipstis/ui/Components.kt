package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.model.toColor
import com.zabbarfalih.sipstis.model.toTextColor
import com.zabbarfalih.sipstis.ui.theme.SipstisTheme
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ItemCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    options: @Composable (() -> Unit) = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(25.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options()
                        }
                    }
                }

                Text(
                    text = description,
                )
            }
        }
    }
}
@Composable
fun EmptyPengajuanCard(
    modifier: Modifier = Modifier,
    title: String
) {
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
fun PengajuanCard(
    pengajuan: Pengajuan,
    onButtonClick: () -> Unit
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
                        color = MaterialTheme.colorScheme.secondary
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
                        color = pengajuan.status.toColor()
                    ) {
                        Text(
                            text = "${pengajuan.status.toFormattedString()}",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W700,
                            color = pengajuan.status.toTextColor()
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
                                color = MaterialTheme.colorScheme.tertiaryContainer
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
                        onClick = onButtonClick,
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
        .split('_')
        .joinToString(" ") { word ->
            when {
                word.equals("PBJ", ignoreCase = true) -> "PBJ"
                word.equals("PPK", ignoreCase = true) -> "PPK"
                word.equals("PEMBELIAN", ignoreCase = true) -> "(Pembelian)"
                else -> word.lowercase(Locale.getDefault()).replaceFirstChar { it.uppercaseChar() }
            }
        }
}

@Composable
fun RoleCard(roleName: String, description: String, icon: ImageVector, navController: NavHostController, screen: SipstisScreen) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate(screen.name) }
            .shadow(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(icon, contentDescription = roleName)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = roleName,
                    fontWeight = FontWeight.W800,
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemCardPreview() {
    SipstisTheme {
        ItemCard(title = "title", description = "description")
    }
}

val textFieldModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 8.dp, vertical = 0.dp)


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


fun convertDateToIndonesianFormat(dateStr: String?): String {
    // Format asli tanggal
    val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Format target (bahasa Indonesia)
    val targetFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))

    return try {
        // Parsing tanggal dari format asli
        val date = originalFormat.parse(dateStr)

        // Memformat ulang tanggal ke format target
        targetFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Format Tanggal Salah" // Atau nilai default lainnya
    }
}
