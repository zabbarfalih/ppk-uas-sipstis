package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily

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
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(
                id = R.string.role_unit
            ),
            style = TextStyle(
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.widthIn(0.dp, 240.dp)
        )

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

        if (unitViewModel.pengajuans.value.isEmpty()) {
            EmptyPengajuanCard(
                title = stringResource(id = R.string.tidak_ada_pengajuan_unit)
            )
        }else {
            LazyColumn {
                items(unitViewModel.pengajuans.value) { pengajuan ->
                    PengajuanCard(pengajuan, onButtonClick = {
                        navController.navigate("${SipstisScreen.DetailPengajuan.name}/${pengajuan.id}")
                    })
                }
            }
        }
    }
}