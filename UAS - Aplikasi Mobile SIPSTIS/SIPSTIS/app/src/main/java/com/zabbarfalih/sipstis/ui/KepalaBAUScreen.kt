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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.model.Pengajuan
import com.zabbarfalih.sipstis.model.PengajuanForm
import com.zabbarfalih.sipstis.model.StatusEnum
import com.zabbarfalih.sipstis.model.toColor
import com.zabbarfalih.sipstis.model.toTextColor
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KepalaBAUScreen(
    kepalaBauViewModel: KepalaBAUViewModel,
    modifier: Modifier = Modifier,
    userState: UserState,
    navController: NavHostController,
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    showSpinner: () -> Unit = {}
) {
    kepalaBauViewModel.fetchPengajuans()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(
                id = R.string.role_kepala_bau
            ),
            style = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.widthIn(0.dp, 240.dp)
        )
        if (kepalaBauViewModel.pengajuans.value.isEmpty()) {
            EmptyPengajuanCard(
                title = stringResource(id = R.string.tidak_ada_pengajuan_kepala_bau)
            )
        } else {
            LazyColumn {
                items(kepalaBauViewModel.pengajuans.value) { pengajuan ->
                    PengajuanCard(pengajuan, onButtonClick = {
                        navController.navigate("${SipstisScreen.DetailPengajuanKepalaBAU.name}/${pengajuan.id}")
                    })
                }
            }
        }
    }
}