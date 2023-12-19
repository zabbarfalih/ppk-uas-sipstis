package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    userState: UserState,
    navController: NavHostController
) {
    var query by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.padding(5.dp))

        UserRolesDisplay(userState.roles, navController)
    }
}

@Composable
fun UserRolesDisplay(roles: List<String>, navController: NavHostController) {
    val roleOrder = listOf(
        "ROLE_UNIT",
        "ROLE_PBJ",
        "ROLE_PPK",
        "ROLE_KEPALA_BAU",
        "ROLE_ADMIN"
    )
    val sortedRoles = roles.sortedBy { roleOrder.indexOf(it) }
    LazyColumn {
        items(sortedRoles) { role ->
            when (role) {
                "ROLE_UNIT" -> RoleCard("Unit",
                    "Menu untuk Mengajukan Pengadaan Barang/Jasa"
                    , Icons.Filled.SwitchAccount, navController, SipstisScreen.Unit)
                "ROLE_PBJ" -> RoleCard(stringResource(R.string.role_pbj_Lengkap),
                    "Menu untuk Melihat Pengajuan yang Diproses PBJ",
                    Icons.Filled.SupervisorAccount, navController, SipstisScreen.PBJ)
                "ROLE_PPK" -> RoleCard(stringResource(R.string.role_ppk_Lengkap),
                    "Menu untuk Melihat Pengajuan yang Diproses PPK",
                    Icons.Filled.SupervisedUserCircle, navController, SipstisScreen.PPK)
                "ROLE_KEPALA_BAU" -> RoleCard("Kepala BAU",
                    "Menu untuk Menyetujui/Menolak/Menolak dengan Revisi Pengajuan oleh Kepala BAU",
                    Icons.Filled.PermContactCalendar, navController, SipstisScreen.KepalaBAU)
            }
        }
    }
}