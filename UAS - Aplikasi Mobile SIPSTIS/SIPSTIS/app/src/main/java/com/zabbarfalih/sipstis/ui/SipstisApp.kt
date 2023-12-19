package com.zabbarfalih.sipstis.ui

import android.app.ProgressDialog
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PermContactCalendar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.data.UserState
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

enum class SipstisScreen {
    Login,
    Register,
    Home,
    Profile,

    Unit,
    CreatePengajuan,
    DetailPengajuan,

    PBJ,
    DetailPengajuanPBJ,

    PPK,
    DetailPengajuanPPK,

    KepalaBAU,
    DetailPengajuanKepalaBAU,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SipstisApp(
    navController: NavHostController = rememberNavController(),
    sipstisAppViewModel: SipstisAppViewModel = viewModel(factory = SipstisAppViewModel.Factory)
) {
    val loggedInUser = sipstisAppViewModel.userState.collectAsState().value
    val uiState = sipstisAppViewModel.uiState.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val showTopBar = when (navBackStackEntry?.destination?.route) {
        SipstisScreen.Login.name, SipstisScreen.Register.name -> false
        else -> true
    }

    if (uiState.value.showProgressDialog) {
        ProgressDialog(onDismissRequest = { sipstisAppViewModel.dismissSpinner() })
    }
    if (uiState.value.showMessageDialog) {
        MessageDialog(
            onDismissRequest = { sipstisAppViewModel.dismissMessageDialog() },
            onClose = { sipstisAppViewModel.dismissMessageDialog() },
            title = uiState.value.messageTitle,
            message = uiState.value.messageBody
        )
    }

    val isUserLoggedIn = loggedInUser.token.isNotEmpty()
    val drawerGesturesEnabled = isUserLoggedIn
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerGesturesEnabled,
        drawerContent = {
            ModalDrawerSheet {
                SipstisDrawer(
                    user = loggedInUser,
                    navController = navController,
                    closeDrawer = {
                        scope.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                    },
                    logout = { sipstisAppViewModel.logout() }
                )
            }
        }
    ) {

        Scaffold(
            topBar = {
                if (showTopBar) {
                    SipstisTopBar(
                        onMenuClicked = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        navController = navController,
                        logout = { sipstisAppViewModel.logout() }
                    )
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = if (loggedInUser.token == "") SipstisScreen.Login.name else SipstisScreen.Home.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = SipstisScreen.Login.name) {
                    LoginScreen(
                        onLoginSuccess = {
                            sipstisAppViewModel.dismissSpinner()
                            navController.navigate(SipstisScreen.Home.name)
                        },
                        onRegisterButtonClicked = { navController.navigate(SipstisScreen.Register.name) },
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) }
                    )
                }

                composable(route = SipstisScreen.Register.name) {
                    RegisterScreen(
                        onLoginButtonClicked = { navController.navigate(SipstisScreen.Login.name) },
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) }
                    )
                }

                composable(route = SipstisScreen.Home.name) {
                    HomeScreen(
                        homeViewModel = viewModel(factory = HomeViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        userState = loggedInUser,
                        navController = navController
                    )
                }

                composable(route = SipstisScreen.Profile.name) {
                    ProfileScreen(
                        email = loggedInUser.email,
                        nip = loggedInUser.nip,
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        navController = navController
                    )
                }

                composable(route = SipstisScreen.Unit.name) {
                    UnitScreen(
                        unitViewModel = viewModel(factory = UnitViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        userState = loggedInUser,
                        navController = navController
                    )
                }

                composable(route = SipstisScreen.CreatePengajuan.name) {
                    CreatePengajuanScreen(
                        createPengajuanViewModel = viewModel(factory = CreatePengajuanViewModel.Factory),
                        unitViewModel = viewModel(factory = UnitViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        navController = navController,
                        context = LocalContext.current
                    )
                }

                composable(
                    route = "${SipstisScreen.DetailPengajuan.name}/{pengajuanId}",
                    arguments = listOf(navArgument("pengajuanId") { type = NavType.LongType })
                ) { backStackEntry ->
                    DetailPengajuanScreen(
                        unitViewModel = viewModel(factory = UnitViewModel.Factory),
                        detailPengajuanViewModel = viewModel(factory = DetailPengajuanViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        navController = navController,
                        navBackStackEntry = backStackEntry,
                        context = LocalContext.current
                    )
                }

                composable(route = SipstisScreen.PBJ.name) {
                    PBJScreen(
                        pbjViewModel = viewModel(factory = PBJViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        userState = loggedInUser,
                        navController = navController
                    )
                }

                composable(
                    route = "${SipstisScreen.DetailPengajuanPBJ.name}/{pengajuanId}",
                    arguments = listOf(navArgument("pengajuanId") { type = NavType.LongType })
                ) { backStackEntry ->
                    DetailPengajuanPBJScreen(
                        pbjViewModel = viewModel(factory = PBJViewModel.Factory),
                        detailPengajuanPbjViewModel = viewModel(factory = DetailPengajuanPBJViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        navController = navController,
                        navBackStackEntry = backStackEntry,
                        context = LocalContext.current
                    )
                }

                composable(route = SipstisScreen.PPK.name) {
                    PPKScreen(
                        ppkViewModel = viewModel(factory = PPKViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        userState = loggedInUser,
                        navController = navController
                    )
                }

                composable(
                    route = "${SipstisScreen.DetailPengajuanPPK.name}/{pengajuanId}",
                    arguments = listOf(navArgument("pengajuanId") { type = NavType.LongType })
                ) { backStackEntry ->
                    DetailPengajuanPPKScreen(
                        ppkViewModel = viewModel(factory = PPKViewModel.Factory),
                        detailPengajuanPpkViewModel = viewModel(factory = DetailPengajuanPPKViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        navController = navController,
                        navBackStackEntry = backStackEntry,
                        context = LocalContext.current
                    )
                }

                composable(route = SipstisScreen.KepalaBAU.name) {
                    KepalaBAUScreen(
                        kepalaBauViewModel = viewModel(factory = KepalaBAUViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        userState = loggedInUser,
                        navController = navController
                    )
                }

                composable(
                    route = "${SipstisScreen.DetailPengajuanKepalaBAU.name}/{pengajuanId}",
                    arguments = listOf(navArgument("pengajuanId") { type = NavType.LongType })
                ) { backStackEntry ->
                    DetailPengajuanKepalaBAUScreen(
                        kepalaBauViewModel = viewModel(factory = KepalaBAUViewModel.Factory),
                        detailPengajuanKepalaBauViewModel = viewModel(factory = DetailPengajuanKepalaBAUViewModel.Factory),
                        showSpinner = { sipstisAppViewModel.showSpinner() },
                        showMessage = { title, body -> sipstisAppViewModel.showMessageDialog(title, body) },
                        navController = navController,
                        navBackStackEntry = backStackEntry,
                        context = LocalContext.current
                    )
                }
            }
        }
    }
}


@Composable
fun SipstisDrawer(
    user: UserState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit = {},
    logout: suspend () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = closeDrawer) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.kembali)
                )
            }

            Text(
                text = stringResource(id = R.string.menu),
                fontWeight = FontWeight.W800
            )
        }

        DrawerNavigationItem(
            Icons.Filled.Home,
            text = R.string.menu_beranda
        ) {
            navController.navigate(SipstisScreen.Home.name)
            closeDrawer()
        }
        if (user.roles.contains("ROLE_UNIT")) {
            DrawerNavigationItem(
                icons = Icons.Filled.SwitchAccount,
                text = R.string.role_unit
            ) {
                navController.navigate(SipstisScreen.Unit.name)
                closeDrawer()
            }
        }
        if (user.roles.contains("ROLE_PBJ")) {
            DrawerNavigationItem(
                icons = Icons.Filled.SupervisorAccount,
                text = R.string.role_pbj
            ) {
                navController.navigate(SipstisScreen.PBJ.name)
                closeDrawer()
            }
        }
        if (user.roles.contains("ROLE_PPK")) {
            DrawerNavigationItem(
                icons = Icons.Filled.SupervisedUserCircle,
                text = R.string.role_ppk
            ) {
                navController.navigate(SipstisScreen.PPK.name)
                closeDrawer()
            }
        }
        if (user.roles.contains("ROLE_KEPALA_BAU")) {
            DrawerNavigationItem(
                icons = Icons.Filled.PermContactCalendar,
                text = R.string.role_kepala_bau
            ) {
                navController.navigate(SipstisScreen.KepalaBAU.name)
                closeDrawer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SipstisTopBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit = {},
    navController: NavHostController,
    closeDrawer: () -> Unit = {},
    logout: suspend () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stis),
                    contentDescription = stringResource(id = R.string.logo_stis),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.padding(6.dp))
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = stringResource(id = R.string.app_name),
                    fontSize =  26.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W800
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.profile)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.background
                )
            ) {
                DrawerNavigationItem(
                    icons = Icons.Filled.Person,
                    text = R.string.menu_edit_profil
                ) {
                    navController.navigate(SipstisScreen.Profile.name)
                    closeDrawer()
                }
                DrawerNavigationItem(
                    icons = Icons.Filled.ExitToApp,
                    text = R.string.logout
                ) {
                    scope.launch {
                        logout()
                        navController.navigate(SipstisScreen.Login.name)
                        closeDrawer()
                    }
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun DrawerNavigationItem(
    icons: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            icons,
            contentDescription = null
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(
            text = stringResource(id = text)
        )
    }
}