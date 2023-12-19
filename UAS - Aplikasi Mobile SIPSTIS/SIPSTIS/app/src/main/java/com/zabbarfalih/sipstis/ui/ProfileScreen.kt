package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.ui.theme.SipstisTheme
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    email: String,
    nip: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory),
    showMessage: (Int, Int) -> Unit = { _, _ -> },
    showSpinner: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    if (profileViewModel.showConfirmDialog) {
        ConfirmDialog(
            onConfirmRequest = {
                profileViewModel.showConfirmDialog = false
                showSpinner()

                scope.launch {
                    when (profileViewModel.deleteAccount()) {
                        DeleteAccountResult.Success -> {
                            showMessage(R.string.sukses, R.string.berhasil_hapus_akun)
                            navController.navigate(SipstisScreen.Login.name)
                        }
                        else -> showMessage(R.string.error, R.string.network_error)
                    }
                }
            },
            onDismissRequest = { profileViewModel.showConfirmDialog = false },
            message = R.string.konfirmasi_hapus_akun
        )
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

            SectionEditProfile(profileViewModel, nip, email, showMessage, showSpinner)

            Spacer(modifier = Modifier.height(24.dp))

            SectionChangePassword(profileViewModel, showMessage, showSpinner)

            Spacer(modifier = Modifier.height(24.dp))

            SectionDeleteAccount(profileViewModel)
        }
    }
}

@Composable
fun SectionEditProfile(
    profileViewModel: ProfileViewModel,
    nip: String,
    email: String,
    showMessage: (Int, Int) -> Unit,
    showSpinner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Text(
        text = stringResource(id = R.string.menu_edit_profil),
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
                value = profileViewModel.nameField,
                onValueChange = { profileViewModel.updateNameField(it) },
                label = { Text(
                    text = stringResource(id = R.string.nama),
                    fontFamily = poppinsFontFamily,
                ) },
                singleLine = true,
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            OutlinedTextField(
                value = nip,
                onValueChange = { },
                label = { Text(
                    text = stringResource(id = R.string.nip),
                    fontFamily = poppinsFontFamily,
                ) },
                singleLine = true,
                modifier = textFieldModifier,
                enabled = false
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { },
                label = { Text(
                    text = stringResource(id = R.string.email),
                    fontFamily = poppinsFontFamily,
                ) },
                singleLine = true,
                modifier = textFieldModifier,
                enabled = false
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            OutlinedTextField(
                value = profileViewModel.noTeleponField,
                onValueChange = { profileViewModel.updateNoTeleponField(it) },
                label = { Text(
                    text = stringResource(id = R.string.no_telp),
                    fontFamily = poppinsFontFamily,
                ) },
                singleLine = true,
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    showSpinner()

                    scope.launch {
                        when (profileViewModel.updateProfile()) {
                            UpdateProfileResult.Success -> showMessage(R.string.sukses, R.string.berhasil_ubah_profil)
                            UpdateProfileResult.Error -> showMessage(R.string.error, R.string.network_error)
                            UpdateProfileResult.EmptyField -> showMessage(R.string.error, R.string.semua_field_harus_valid)
                        }
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.ubah_profil),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun SectionChangePassword(
    profileViewModel: ProfileViewModel,
    showMessage: (Int, Int) -> Unit,
    showSpinner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    Text(
        text = stringResource(id = R.string.ubah_password),
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
            PasswordTextField(
                value = profileViewModel.oldPasswordField,
                onValueChange = { profileViewModel.updateOldPasswordField(it) },
                label = {
                    Text(text = stringResource(id = R.string.password_lama),
                        fontFamily = poppinsFontFamily
                    )
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            PasswordTextField(
                value = profileViewModel.newPasswordField,
                onValueChange = { profileViewModel.updateNewPasswordField(it) },
                label = {
                    Text(text = stringResource(id = R.string.password_baru),
                        fontFamily = poppinsFontFamily
                    )
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )

            Spacer(modifier = Modifier.padding(top = 4.dp))

            PasswordTextField(
                value = profileViewModel.confirmPasswordField,
                onValueChange = { profileViewModel.updateConfirmPasswordField(it) },
                label = {
                    Text(text = stringResource(id = R.string.konfirmasi_password),
                        fontFamily = poppinsFontFamily
                    )
                },
                modifier = textFieldModifier,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                showSpinner()

                scope.launch {
                    when (profileViewModel.updatePassword()) {
                        UpdatePasswordResult.Success -> showMessage(R.string.sukses, R.string.berhasil_ubah_password)
                        UpdatePasswordResult.WrongOldPassword -> showMessage(R.string.error, R.string.password_lama_salah)
                        UpdatePasswordResult.Mismatch -> showMessage(R.string.error, R.string.password_mismatch)
                        UpdatePasswordResult.Error -> showMessage(R.string.error, R.string.network_error)
                        UpdatePasswordResult.EmptyField -> showMessage(R.string.error, R.string.semua_field_harus_valid)
                    }
                }
            }) {
                Text(
                    text = stringResource(id = R.string.ubah_password),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700,
                    fontSize = 13.sp
                )
            }
        }
    }
}
@Composable
fun SectionDeleteAccount(
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Text(
            text = stringResource(id = R.string.hapus_akun),
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
                Button(
                    onClick = { profileViewModel.showConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.hapus_akun),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.W700,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}