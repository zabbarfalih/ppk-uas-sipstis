package com.zabbarfalih.sipstis.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.zabbarfalih.sipstis.R
import com.zabbarfalih.sipstis.ui.theme.SipstisTheme
import com.zabbarfalih.sipstis.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onLoginButtonClicked: () -> Unit = {},
    registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory),
    showSpinner: () -> Unit = {},
    showMessage: (Int, Int) -> Unit = { _, _ -> }
) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.stis),
                contentDescription = stringResource(id = R.string.logo_stis),
                modifier = Modifier.height(36.dp)
            )

            Spacer(Modifier.width(8.dp))
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W800,
                    fontSize = 36.sp,
                ),
            )
        }
    }

    Box(
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(top = 86.dp)
        ) {
            Text(
                text = stringResource(id = R.string.daftar),
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700,
                    fontSize = 26.sp,
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
                    modifier = modifier.padding(8.dp)
                        .fillMaxWidth()
                ) {
                    val textFieldModifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 0.dp)

                    OutlinedTextField(
                        value = registerViewModel.nameField,
                        onValueChange = { registerViewModel.updateNameField(it) },
                        label = { Text(text = stringResource(id = R.string.nama)) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = registerViewModel.nipField,
                        onValueChange = { registerViewModel.updateNipField(it) },
                        label = { Text(text = stringResource(id = R.string.nip)) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = registerViewModel.emailField,
                        onValueChange = { registerViewModel.updateEmailField(it) },
                        label = { Text(text = stringResource(id = R.string.email)) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    OutlinedTextField(
                        value = registerViewModel.noTeleponField,
                        onValueChange = { registerViewModel.updateNoTeleponField(it) },
                        label = { Text(text = stringResource(id = R.string.no_telp)) },
                        singleLine = true,
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Phone
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    PasswordTextField(
                        value = registerViewModel.passwordField,
                        onValueChange = { registerViewModel.updatePasswordField(it) },
                        label = { Text(text = stringResource(id = R.string.password)) },
                        modifier = textFieldModifier,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    PasswordTextField(
                        value = registerViewModel.confirmPasswordField,
                        onValueChange = { registerViewModel.updateConfirmPasswordField(it) },
                        label = { Text(text = stringResource(id = R.string.konfirmasi_password)) },
                        modifier = textFieldModifier,                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Spacer(modifier = Modifier.padding(top = 24.dp))

                    Button(
                        onClick = {
                            showSpinner()
                            coroutineScope.launch {
                                when (registerViewModel.register()) {
                                    RegisterResult.Success -> showMessage(R.string.sukses, R.string.berhasil_buat_akun)
                                    RegisterResult.EmptyField -> showMessage(R.string.error, R.string.semua_field_harus_valid)
                                    RegisterResult.PasswordMismatch -> showMessage(R.string.error, R.string.password_mismatch)
                                    else -> showMessage(R.string.error, R.string.network_error)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.daftar),
                            style = TextStyle(
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.W700,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp));

            Row{
                Text(
                    text = stringResource(id = R.string.sudah_punya_akun),
                    style = TextStyle(
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )

                Spacer(modifier = Modifier.padding(2.dp));

                Text(
                    text = stringResource(id = R.string.masuk),
                    style = TextStyle(
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.W700,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                        ),
                    modifier = Modifier.clickable { onLoginButtonClicked() }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterScreenPreview() {
    SipstisTheme {
        RegisterScreen()
    }
}